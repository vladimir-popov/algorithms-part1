import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.RectHV
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.rng.Seed

trait Utils {

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

  val createBrutForce = () =>
    new PointSET() with PointsSet { def name = "Brut force" }

  val createKdTree = () => new KdTree() with PointsSet { def name = "KdTree" }
}
