import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import scala.jdk.CollectionConverters._

class SolverTest extends AnyFreeSpec with Matchers {
  val solvable = new Board(
    Array(
      Array(0, 1, 3),
      Array(4, 2, 5),
      Array(7, 8, 6)
    )
  )
  val unsolvable: Board = new Board(
    Array(
      Array(1, 2, 3),
      Array(4, 5, 6),
      Array(8, 7, 0)
    )
  )
  "Corner cases" - {
    "Throw an IllegalArgumentException in the constructor if the argument is null" in {
      assertThrows[IllegalArgumentException] {
        new Solver(null)
      }
    }
    "Return false in isSolvable() if the board is unsolvable" in {
      val s = new Solver(unsolvable)
      s.isSolvable() shouldBe false
    }
    "Return -1 in moves() if the board is unsolvable" in {
      val s = new Solver(unsolvable)
      s.moves() shouldBe -1
    }
    "Return null in solution() if the board is unsolvable" in {
      val s = new Solver(unsolvable)
      s.solution() shouldBe null
    }
  }
  "Twin" - {
    "twin of unsolvable board should be solvable" in {
      val s = new Solver(unsolvable.twin())
      s.isSolvable() shouldBe true
    }
  }
  "Test cases" - {
    "puzzle04" in {
      val expectation = Seq(
        new Board(Array(Array(0, 1, 3), Array(4, 2, 5), Array(7, 8, 6))),
        new Board(Array(Array(1, 0, 3), Array(4, 2, 5), Array(7, 8, 6))),
        new Board(Array(Array(1, 2, 3), Array(4, 0, 5), Array(7, 8, 6))),
        new Board(Array(Array(1, 2, 3), Array(4, 5, 0), Array(7, 8, 6))),
        new Board(Array(Array(1, 2, 3), Array(4, 5, 6), Array(7, 8, 0)))
      )
      withSolver(new Solver(solvable)) { s =>
        val solution = s.solution()
        solution.asScala.toList should have size expectation.size
        solution.asScala.toList.zip(expectation).foreach {
          case (actual, expected) =>
            assert(
              actual equals expected,
              s"Wrong board: \n$actual\nexpected:\n$expected"
            )
        }
      }
    }
  }
  "puzzle17" in {
    val s = new Solver(
      new Board(Array(Array(5, 1, 8), Array(2, 7, 3), Array(4, 0, 6)))
    );
    withSolver(s) {
      _.moves() shouldBe 17
    }
  }
  "puzzle27" in {
    val s = new Solver(
      new Board(Array(Array(5, 8, 7), Array(1, 4, 6), Array(3, 0, 2)))
    );
    withSolver(s) {
      _.solution() should not be null
    }
  }
  "puzzle28" in {
    val s = new Solver(
      new Board(Array(Array(7, 8, 5), Array(4, 0, 2), Array(3, 6, 1)))
    );
    withSolver(s) {
      _.solution() should not be null
    }
  }

  private def withSolver[A](s: Solver)(test: Solver => A): A = {
    def toString(solution: java.lang.Iterable[Board]) = {
      val s = new StringBuilder("Solution:\n")
      var move = 0
      solution.asScala.foreach { b =>
        move += 1
        s.append(move)
        s.append(b.toString.tail)
        s.append("\n")
      }
      s.toString()
    }
    withClue(toString(s.solution()))(test(s))
  }
}
