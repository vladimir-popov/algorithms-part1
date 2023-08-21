import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import scala.jdk.CollectionConverters._

class FindSuccessorTest extends AnyFreeSpec with Matchers with TableDrivenPropertyChecks {
  "get" in {
    val successor = new FindSuccessor(10)
    successor.deleteAll(Array(0, 2, 3, 9))
    val cases = Table(("Get", "Expect"), 4 -> 4, 9 -> -1, 3 -> 4, 0 -> 1, 2 -> 4)
    forAll(cases) { case (i, expected) =>
      successor.get(i) shouldBe expected
    }
  }
}
