import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.RectHV
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.jdk.CollectionConverters._

class KdTreeTest extends AnyFreeSpec with Matchers {
  val brutForce = () => new PointSET() with PointsSet { def name = "Brut force" }
  val kdTree = () => new KdTree() with PointsSet { def name = "KdTree" }

  Seq(brutForce, kdTree).foreach { pointsSet =>
    pointsSet().name - {

      "should return null as nearest if set is empty" in {
        pointsSet().nearest(new Point2D(0.0, 0.0)) shouldBe null
      }

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

        "should return the nearest points" in {
          val result = points.nearest(new Point2D(0.1, 0.1))
          result shouldBe new Point2D(0.2, 0.3)
        }
      }
    }
  }
}
