import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalactic.Prettifier
import org.scalatest.compatible.Assertion
import scala.jdk.CollectionConverters._

class BoardTest extends AnyFreeSpec with Matchers {

  "equals" - {
    val a = new Board(
      Array(
        Array(1, 0, 3),
        Array(4, 2, 5),
        Array(7, 8, 6)
      )
    )
    val b = new Board(
      Array(
        Array(8, 1, 3),
        Array(4, 0, 2),
        Array(7, 6, 5)
      )
    )
    val b1 = new Board(
      Array(
        Array(8, 1, 3),
        Array(4, 0, 2),
        Array(7, 6, 5)
      )
    )
    "a board should not be equal to null" in {
      assertNot(a equals null)
    }
    "different boards should not be equal" in {
      assertNot(a equals b)
    }
    "the same board should be equal to itself" in {
      assert(b equals b)
    }
    "two boards with the same size and same positions should be equal" in {
      assert(b equals b1)
    }
  }

  "toString" in {
    val b = new Board(
      Array(
        Array(1, 0, 3),
        Array(4, 2, 5),
        Array(7, 8, 6)
      )
    )
    b.toString() shouldBe """|3
    |1 0 3
    |4 2 5
    |7 8 6""".stripMargin
  }

  "isGoal" - {
    "should return true for a solved board" in {
      val b = new Board(
        Array(
          Array(1, 2, 3),
          Array(4, 5, 6),
          Array(7, 8, 0)
        )
      )
      b.isGoal() shouldBe true
    }
    "should return false for a not solved board" in {
      val b = new Board(
        Array(
          Array(0, 2, 3),
          Array(4, 5, 6),
          Array(7, 8, 1)
        )
      )
      b.isGoal() shouldBe false
    }
  }

  "Hamming distance" in {
    val b = new Board(
      Array(
        Array(8, 1, 3),
        Array(4, 0, 2),
        Array(7, 6, 5)
      )
    )
    b.hamming() shouldBe 5
  }

  "Manhattan distance" in {
    val b = new Board(
      Array(
        Array(8, 1, 3),
        Array(4, 0, 2),
        Array(7, 6, 5)
      )
    )
    b.manhattan() shouldBe 10
  }

  "Twin" - {
    "should swap the first tiles" in {
      val b = new Board(
        Array(
          Array(8, 1, 3),
          Array(4, 0, 2),
          Array(7, 6, 5)
        )
      )
      b.twin() shouldBe new Board(
        Array(
          Array(1, 8, 3),
          Array(4, 0, 2),
          Array(7, 6, 5)
        )
      )
    }
    "should swap the first non empty tiles" in {
      val b = new Board(
        Array(
          Array(0, 1, 3),
          Array(4, 8, 2),
          Array(7, 6, 5)
        )
      )
      b.twin() shouldBe new Board(
        Array(
          Array(0, 3, 1),
          Array(4, 8, 2),
          Array(7, 6, 5)
        )
      )
    }
  }

  "Neighboring boards" in {
    val b = new Board(
      Array(
        Array(8, 1, 3),
        Array(4, 0, 2),
        Array(7, 6, 5)
      )
    )
    val neighbors = Seq(
      Array(
        Array(8, 0, 3),
        Array(4, 1, 2),
        Array(7, 6, 5)
      ),
      Array(
        Array(8, 1, 3),
        Array(4, 2, 0),
        Array(7, 6, 5)
      ),
      Array(
        Array(8, 1, 3),
        Array(4, 6, 2),
        Array(7, 0, 5)
      ),
      Array(
        Array(8, 1, 3),
        Array(0, 4, 2),
        Array(7, 6, 5)
      )
    ).map(new Board(_))

    b.neighbors().asScala should contain theSameElementsAs neighbors
  }

  inline def assertNot(inline condition: Boolean): Assertion =
    assert(!condition)
}
