import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.jdk.CollectionConverters._

class DequeTest extends AnyFreeSpec with Matchers {

  "Throw" - {
    val d = new Deque[String]()
    "IllegalArgumentException if the client passes the null to" - {
      "addFirst" in
        assertThrows[IllegalArgumentException] {
          d.addFirst(null)
        }

      "addLast" in
        assertThrows[IllegalArgumentException] {
          d.addLast(null)
        }
    }
    "NoSuchElementException if the client calls" - {
      "removeFirst on an empty Deque" in {
        val emptyDeque = new Deque[Int]()
        assertThrows[NoSuchElementException] {
          emptyDeque.removeFirst()
        }
      }
      "removeLast on an empty Deque" in {
        val emptyDeque = new Deque[Int]()
        assertThrows[NoSuchElementException] {
          emptyDeque.removeLast()
        }
      }
      "next on an empty iterator" in {
        val emptyDeque = new Deque[Int]()
        assertThrows[NoSuchElementException] {
          emptyDeque.iterator().next()
        }
      }
    }
    "UnsupportedOperationException if the client calls" - {
      "remove on iterator" in
        assertThrows[UnsupportedOperationException] {
          d.iterator().remove()
        }
    }
  }

  "A new Deque" - {
    val d = new Deque[Int]()
    "should be empty" in {
      d.isEmpty() shouldBe true
    }
    "should has size 0" in {
      d.size() shouldBe 0
    }
    "should return an empty iterator" in {
      d.iterator().hasNext() shouldBe false
    }
  }

  "Iterator" - {
    "should return elements in the direct order" in {
      val d = new Deque[Int]()
      d.addFirst(3)
      d.addFirst(2)
      d.addFirst(1)
      d.asScala.toSeq shouldBe Seq(1, 2, 3)
    }
  }

  "Adding elements" - {
    "addFirst should increment size of the Deque" in {
      val d = new Deque[Int]()
      d.addFirst(1)
      d.addFirst(2)
      d.addFirst(3)

      d.size() shouldBe 3
    }
    "addLast should increment size of the Deque" in {
      val d = new Deque[Int]()
      d.addLast(1)
      d.addLast(2)
      d.addLast(3)

      d.size() shouldBe 3
    }
    "calling the addFirst should be equal to calling the addLast in reverse order" in {
      val d = new Deque[Int]()
      d.addFirst(1)
      d.addFirst(2)
      d.addFirst(3)

      val r = new Deque[Int]()
      r.addLast(3)
      r.addLast(2)
      r.addLast(1)

      d.asScala.toSeq shouldBe r.asScala.toSeq
    }
    "removeFirst should remove the last added element" in {
      val d = new Deque[Int]()
      d.addFirst(1)
      d.addFirst(2)
      d.addFirst(3)
      d.removeFirst()
      d.asScala.toSeq shouldBe Seq(2, 1)
    }
    "removeLast should remove the first added element" in {
      val d = new Deque[Int]()
      d.addFirst(1)
      d.addFirst(2)
      d.addFirst(3)
      d.removeLast()
      d.asScala.toSeq shouldBe Seq(3, 2)
    }
  }
}
