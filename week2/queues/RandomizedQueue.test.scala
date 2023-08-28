import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.concurrent.Eventually
import scala.jdk.CollectionConverters._

class RandomizedQueueTest extends AnyFreeSpec with Matchers with Eventually {

  "Throw IllegalArgumentException if the client passes the null to enqueue method" in {
    val q = new RandomizedQueue[String]()
    assertThrows[IllegalArgumentException] {
      q.enqueue(null)
    }
  }
  "Throw NoSuchElementException if the client calls on an empty queque method" - {
    val emptyQueque = new RandomizedQueue[Int]()
    "sample" in {
      assertThrows[NoSuchElementException] {
        emptyQueque.sample();
      }
    }
    "dequeue" in {
      assertThrows[NoSuchElementException] {
        emptyQueque.dequeue();
      }
    }
    "iterator.next" in {
      assertThrows[NoSuchElementException] {
        emptyQueque.iterator().next();
      }
    }
  }

  "A new RandomizedQueue" - {
    val q = new RandomizedQueue[Int]()
    "should be empty" in {
      q.isEmpty() shouldBe true
    }
    "should has size 0" in {
      q.size() shouldBe 0
    }
    "should return an empty iterator" in {
      q.iterator().hasNext() shouldBe false
    }
  }

  "The dequeue method" - {
    "should return a random item" in {
      eventually {
        val q = new RandomizedQueue[Int]()
        q.enqueue(1)
        q.enqueue(2)
        q.enqueue(3)
        q.dequeue() should not be (1)
      }
    }
    "should decrease the size of the queue" in {
      val q = new RandomizedQueue[Int]()
      q.enqueue(1)
      q.enqueue(2)
      q.enqueue(3)
      q.size() shouldBe 3
      q.dequeue()
      q.size() shouldBe 2
    }
  }

  "The sample method should return random items" in {
    val q = new RandomizedQueue[Int]()
    q.enqueue(1)
    q.enqueue(2)
    q.enqueue(3)
    q.sample() should not be equal(q.sample())
  }

  "Iterator" - {
    "should return items in random order" in {
      val q = new RandomizedQueue[Int]()
      q.enqueue(1)
      q.enqueue(2)
      q.enqueue(3)
      q.enqueue(4)
      q.enqueue(5)
      eventually {
        q.asScala.toSeq should not be equal(Seq(5, 4, 3, 2, 1))
      }
    }
    "different iterators should have different random order" in {
      val q = new RandomizedQueue[Int]()
      q.enqueue(1)
      q.enqueue(2)
      q.enqueue(3)
      q.enqueue(4)
      q.enqueue(5)
      eventually {
        q.asScala.toSeq should not be equal(q.asScala.toSeq)
      }
    }
  }
}
