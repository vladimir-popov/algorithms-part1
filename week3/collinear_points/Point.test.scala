import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

class PointTest
    extends AnyFreeSpec
    with Matchers
    with ScalaCheckPropertyChecks {

  implicit val arbPoint: Arbitrary[Point] = Arbitrary(
    for {
      x <- Gen.choose(0, 512)
      y <- Gen.choose(0, 512)
    } yield new Point(x, y)
  )

  "compareTo" - {
    "lower point should be greater" in {
      val p1 = new Point(0, 1)
      val p2 = new Point(0, 2)
      p1.compareTo(p2) should be < 0
      p2.compareTo(p1) should be > 0
    }
    "right point should be greater" in {
      val p1 = new Point(1, 0)
      val p2 = new Point(2, 0)
      p1.compareTo(p2) should be < 0
      p2.compareTo(p1) should be > 0
    }
  }
  "slopeTo" - {
    "for horizontal line should return +0" in {
      val p1 = new Point(0, 0)
      val p2 = new Point(10, 0)
      p1.slopeTo(p2) shouldBe 0.0
      p2.slopeTo(p1) shouldBe 0.0
    }
    "for vertical line should return +infinity" in {
      val p1 = new Point(0, 0)
      val p2 = new Point(0, 10)
      p1.slopeTo(p2) shouldBe Double.PositiveInfinity
      p2.slopeTo(p1) shouldBe Double.PositiveInfinity
    }
    "for point itself should return -infinity" in {
      val p1 = new Point(0, 0)
      val p2 = new Point(0, 0)
      p1.slopeTo(p2) shouldBe Double.NegativeInfinity
      p2.slopeTo(p1) shouldBe Double.NegativeInfinity
    }
  }
  "slopeOrder" - {
    "should be invariant with slopeTo" in {
      forAll { (p0: Point, p1: Point, p2: Point) =>
        val c = p0.slopeOrder()
        val s1 = p0.slopeTo(p1)
        val s2 = p0.slopeTo(p2)
        c.compare(p1, p2) shouldBe s1.compareTo(s2)
      }
    }
  }
}
