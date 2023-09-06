import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalactic.Equality
import org.scalatest.enablers.Aggregating

class CollinearPointsTest extends AnyFreeSpec with Matchers {

  implicit val lineSegmentEquality: Equality[LineSegment] =
    new Equality[LineSegment] {
      override def areEqual(a: LineSegment, b: Any): Boolean = b match {
        case other: LineSegment =>
          (((a.p compareTo other.p) - (a.q compareTo other.q)) == 0) ||
          (((a.p compareTo other.q) - (a.q compareTo other.p)) == 0)
        case _ => false
      }
    }

  trait CollinearPoints {
    def numberOfSegments(): Int
    def segments(): Array[LineSegment]
  }

  "BruteCollinearPoints" ignore testCollinearPoints(points =>
    new BruteCollinearPoints(points) with CollinearPoints
  )

  "FastCollinearPoints" - testCollinearPoints(points =>
    new FastCollinearPoints(points) with CollinearPoints
  )

  def testCollinearPoints(collinearPoints: Array[Point] => CollinearPoints) = {
    "Constructor" - {
      "should throw IllegalArgumentException when the argument" - {
        "is null" in {
          assertThrows[IllegalArgumentException] {
            collinearPoints(null)
          }
        }

        "contains null" in {
          assertThrows[IllegalArgumentException] {
            collinearPoints(
              Array[Point](new Point(0, 0), null, new Point(1, 1))
            )
          }
        }

        "contains a repeated point" in {
          assertThrows[IllegalArgumentException] {
            collinearPoints(
              Array[Point](new Point(0, 0), new Point(0, 0))
            )
          }
        }
      }
    }

    "Test cases" - {
      "test case 1" in {
        val points = Array(
          new Point(2, 1),
          new Point(3, 3),
          new Point(1, 4),
          new Point(5, 2),
          new Point(7, 1)
        )
        val expectedSegments = Array(
          new LineSegment(new Point(1, 4), new Point(7, 1))
        )

        val cp = collinearPoints(points);

        withClue(cp.segments().mkString("\n") + "\n") {
          cp.segments() should contain theSameElementsAs (expectedSegments)
          cp.numberOfSegments() shouldBe 1
        }
      }
      
      // TODO: add case with triangle

      "test case input 6" in {
        val points = Array(
          new Point(19000, 10000),
          new Point(18000, 10000),
          new Point(32000, 10000),
          new Point(21000, 10000),
          new Point(1234, 5678),
          new Point(14000, 10000)
        )
        val expectedSegments = Array(
          new LineSegment(new Point(14000, 10000), Point(32000, 10000))
        )

        val cp = collinearPoints(points);

        withClue(cp.segments().mkString("\n") + "\n") {
          cp.segments() should contain theSameElementsAs (expectedSegments)
          cp.numberOfSegments() shouldBe 1
        }
      }
      "test case input 8" in {
        val points = Array(
          new Point(10000, 0),
          new Point(0, 10000),
          new Point(3000, 7000),
          new Point(7000, 3000),
          new Point(20000, 21000),
          new Point(3000, 4000),
          new Point(14000, 15000),
          new Point(6000, 7000)
        )
        val expectedSegments = Array(
          new LineSegment(new Point(3000, 4000), new Point(20000, 21000)),
          new LineSegment(new Point(0, 10000), new Point(10000, 0))
        )

        val cp = collinearPoints(points);

        withClue(cp.segments().mkString("\n") + "\n") {
          cp.segments() should contain theSameElementsAs (expectedSegments)
          cp.numberOfSegments() shouldBe 2
        }
      }
    }
  }
}
