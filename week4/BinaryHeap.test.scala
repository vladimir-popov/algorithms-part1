import java.util.Comparator
import java.lang.reflect.Field

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester
import org.scalatest.compatible.Assertion

class BinaryHeapTest extends AnyFreeSpec with Matchers {

  "Validate nodes after creation" in {
    val bh = new BinaryHeap[Int](9, Integer.compare)
    (1 until 10).reverse.foreach(bh.add)

    bh.size() shouldBe 9
    validateNodes(bh)
  }

  "Validate nodes after removing the max element" in {
    val bh = new BinaryHeap[Int](9, Integer.compare)
    (1 until 10).reverse.foreach(bh.add)
    val max = bh.removeMax()

    max shouldBe 9
    bh.size() shouldBe 8
    validateNodes(bh)
  }

  private def validateNodes[A: Ordering](bh: BinaryHeap[A]) = {
    val ns = nodes(bh)
    def checkChildren(k: Int): Unit = {
      if (2 * k < (bh.size - 1)) {
        withClue(s"k = $k; ${ns.mkString("[", ", ", "]")}: ") {
          ns(k) should be >= ns(2 * k)
          ns(k) should be >= ns(2 * k + 1)
        }
        checkChildren(2 * k)
        checkChildren(2 * k + 1)
      }
    }
    checkChildren(1)
  }

  private def nodes[A](bh: BinaryHeap[A]): Array[A] = {
    val fnodes = bh.getClass.getDeclaredField("nodes")
    fnodes.setAccessible(true)
    fnodes.get(bh).asInstanceOf[Array[A]].take(bh.size())
  }
}
