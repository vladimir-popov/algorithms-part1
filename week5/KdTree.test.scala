import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.RectHV
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.jdk.CollectionConverters._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen

class KdTreeTest
    extends AnyFreeSpec
    with Matchers
    with ScalaCheckPropertyChecks {

  implicit val arbPoint2D: Arbitrary[Point2D] = Arbitrary {
    for {
      x <- Gen.choose(0.0, 1.0)
      y <- Gen.choose(0.0, 1.0)
    } yield new Point2D(x, y)
  }

  implicit val arbRectHV: Arbitrary[RectHV] = Arbitrary {
    for {
      x <- Gen.choose(0.0, 0.5)
      y <- Gen.choose(0.0, 0.5)
      dx <- Gen.choose(0.0, 0.5)
      dy <- Gen.choose(0.0, 0.5)
    } yield new RectHV(x, y, x + dx, y + dy)
  }

  val brutForce = () =>
    new PointSET() with PointsSet { def name = "Brut force" }

  val kdTree = () => new KdTree() with PointsSet { def name = "KdTree" }

  Seq(brutForce, kdTree ).foreach { pointsSet =>
    pointsSet().name - {

      "should throw the IllegalArgumentException if null passed to" - {
        "insert" in
          assertThrows[IllegalArgumentException] {
            pointsSet().insert(null)
          }
        "contains" in
          assertThrows[IllegalArgumentException] {
            pointsSet().contains(null)
          }
        "range" in
          assertThrows[IllegalArgumentException] {
            pointsSet().range(null)
          }
        "nearest" in
          assertThrows[IllegalArgumentException] {
            pointsSet().nearest(null)
          }
      }

      "Method nearest" - {
        "should return null if the set is empty" in {
          pointsSet().nearest(new Point2D(0.0, 0.0)) shouldBe null
        }
        "should return the nearest point" in {
          // given:
          forAll { (point: Point2D, points: Seq[Point2D]) =>
            whenever(points.nonEmpty) {
              val set = pointsSet()
              points.foreach(set.insert)
              // when:
              val result = set.nearest(point)
              // then:
              points.map(point.distanceTo).min shouldBe result.distanceTo(point)
            }
          }
        }
      }

      "Method range" - {
        "should return empty result if the set is empty" in {
          forAll { (rect: RectHV) =>
            pointsSet().range(rect).asScala shouldBe empty
          }
        }
        "should return all points inside a rectangle" in {
          // given:
          forAll { (rect: RectHV, points: Set[Point2D]) =>
            whenever(points.nonEmpty) {
              val set = pointsSet()
              points.foreach(set.insert)
              // when:
              val inside = set.range(rect).asScala.toSet
              // then:
              val outside = points.diff(inside)
              inside.foreach { p => assert(rect.contains(p)) }
              outside.foreach { p => assert(!rect.contains(p)) }
            }
          }
        }
        "points on the border case" in {
          // given:
          val rect = new RectHV(0.0, 0.0, 1.0, 1.0)
          val points = Seq(
            new Point2D(0.0, 0.0),
            new Point2D(0.0, 1.0),
            new Point2D(1.0, 1.0),
            new Point2D(1.0, 0.0),
            new Point2D(0.0, 0.0)
          )
          val set = pointsSet()
          points.foreach(set.insert)
          // when:
          val result = set.range(rect).asScala
          // then:
          result should contain allElementsOf (points)
        }
      }

      "Simple case" - {
        // given:
        // see https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php
        val points = pointsSet()
        points.insert(new Point2D(0.7, 0.2))
        points.insert(new Point2D(0.5, 0.4))
        points.insert(new Point2D(0.2, 0.3))
        points.insert(new Point2D(0.4, 0.7))
        points.insert(new Point2D(0.9, 0.6))

        "should has size 5" in {
          points.size() shouldBe 5
        }

        "should not increase size if a point already exists" in {
          points.insert(new Point2D(0.7, 0.2))
          points.size() shouldBe 5
        }

        "should contains already inserted point" in {
          points.contains(new Point2D(0.7, 0.2)) shouldBe true
        }

        "should not contains absent point" in {
          points.contains(new Point2D(0.8, 0.3)) shouldBe false
        }

        "should contains only expected points in the range" in {
          val result =
            points.range(new RectHV(0.2, 0.2, 0.7, 0.7)).asScala.toList
          result should contain only (
            new Point2D(0.7, 0.2),
            new Point2D(0.5, 0.4),
            new Point2D(0.4, 0.7),
            new Point2D(0.2, 0.3)
          )
        }
      }
    }
  }
}
