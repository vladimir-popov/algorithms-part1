import java.util.Comparator
import java.lang.reflect.Field

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester
import org.scalatest.compatible.Assertion
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import java.util.Arrays
import java.util.Random
import org.scalacheck.Gen
import org.scalacheck.Arbitrary

class BinaryHeapTest
    extends AnyFreeSpec
    with Matchers
    with ScalaCheckPropertyChecks {

  "Validate nodes after creation" in {
    val bh = new BinaryHeap[Int](9, Integer.compare)
    (1 to 9).reverse.foreach(bh.add)

    bh.size() shouldBe 9
    validateNodes(bh)
  }

  "Validate nodes after removing the max element" in {
    val bh = new BinaryHeap[Int](9, Integer.compare)
    (1 to 9).reverse.foreach(bh.add)
    val max = bh.removeMax()

    max shouldBe 9
    bh.size() shouldBe 8
    validateNodes(bh)
  }

  "HeapSort" - {
    "constant array" in {
      val arr: Array[Integer] = Array(8, 2, 7, 4, 5, 6, 3, 1, 9)
      BinaryHeap.sort(arr, Integer.compare)
      arr shouldBe sorted
    }

    "arbitrary array" in {
      implicit val integerArb: Arbitrary[Integer] =
        Arbitrary(Arbitrary.arbitrary[Int].map(Integer.valueOf))

      forAll { (xs: Array[Integer]) =>
        BinaryHeap.sort(xs, Integer.compare)
        xs shouldBe sorted
      }
    }
  }

  private def validateNodes[A: Ordering](bh: BinaryHeap[A]) = {
    val ns = nodes(bh)
    def checkChildren(k: Int): Unit = {
      withClue(s"k = $k; ${ns.mkString("[", ", ", "]")}: ") {
        if (2 * k < bh.size - 1) {
          ns(k) should be >= ns(2 * k + 1)
          checkChildren(2 * k + 1)
        }
        if (2 * k < bh.size) {
          ns(k) should be >= ns(2 * k)
          checkChildren(2 * k)
        }
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
