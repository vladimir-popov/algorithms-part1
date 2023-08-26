//> using test.dep org.scalatest::scalatest::3.2.16
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import scala.jdk.CollectionConverters._

class WeightedQuickUnionTest extends AnyFreeSpec with Matchers with TableDrivenPropertyChecks {

  // given:
  val groups = List(group(0, 3, 4), group(1, 2, 6, 9), group(5, 7), group(8)).asJava
  val union  = new WeightedQuickUnion(10, groups)

  "find" in {
    val cases = Table(("Node", "Expected root"), 3 -> 4, 2 -> 9, 7 -> 7, 8 -> 8)
    forAll(cases) { case (node, expectedRoot) =>
      union.find(node) shouldBe expectedRoot
    }
  }

  private def group(xs: Int*): Array[Integer] =
    xs.toArray.map(new Integer(_))
}
