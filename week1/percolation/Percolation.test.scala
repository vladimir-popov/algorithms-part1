import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.PrivateMethodTester

class PercolationTest
    extends AnyFreeSpec
    with Matchers
    with TableDrivenPropertyChecks
    with PrivateMethodTester {

  "Constructor" - {
    "should throw IllegalArgumentException if n <= 0" in {
      assertThrows[IllegalArgumentException] {
        new Percolation(-1);
      }
      assertThrows[IllegalArgumentException] {
        new Percolation(0);
      }
    }
    "every site should be closed" in {
      val N = 10
      val p = new Percolation(N)
      (1 to N).foreach { row =>
        (1 to N).foreach { col =>
          p.isOpen(row, col) shouldBe false
        }
      }
      p.numberOfOpenSites() shouldBe 0
    }
  }

  "Method open" - {
    "should throw IllegalArgumentException if one of arguments is < 1 or > N" in {
      val N = 10
      val p = new Percolation(N)
      forAll(
        Table(
          "Row" -> "Col",
          -1 -> 1,
          1 -> -1,
          0 -> 1,
          1 -> 0,
          1 -> (N + 1),
          (N + 1) -> 1
        )
      ) { case (row, col) =>
        assertThrows[IllegalArgumentException] {
          p.open(row, col)
        }
      }
    }
    "should increment count of opened sites only once" in {
      val p = new Percolation(10)
      p.open(1, 1)
      p.numberOfOpenSites() shouldBe 1
      p.open(1, 1)
      p.numberOfOpenSites() shouldBe 1
    }
  }

  "Method isOpen" - {
    "should throw IllegalArgumentException if one of arguments is < 1 or > N" in {
      val N = 10
      val p = new Percolation(N)
      forAll(
        Table(
          "Row" -> "Col",
          -1 -> 1,
          1 -> -1,
          0 -> 1,
          1 -> 0,
          1 -> (N + 1),
          (N + 1) -> 1
        )
      ) { case (row, col) =>
        assertThrows[IllegalArgumentException] {
          p.isOpen(row, col)
        }
      }
    }
    "should return true only for opened sites" in {
      val p = new Percolation(2)
      p.open(1, 1)
      p.isOpen(1, 1) shouldBe true
      p.isOpen(1, 2) shouldBe false
    }
  }

  "Method isFull" - {
    "should throw IllegalArgumentException if one of arguments is < 1 or > N" in {
      val N = 10
      val p = new Percolation(N)
      forAll(
        Table(
          "Row" -> "Col",
          -1 -> 1,
          1 -> -1,
          0 -> 1,
          1 -> 0,
          1 -> (N + 1),
          (N + 1) -> 1
        )
      ) { case (row, col) =>
        assertThrows[IllegalArgumentException] {
          p.isFull(row, col)
        }
      }
    }
  }

  "test case 1" - {

    val sites =
      """□▣▣
        |▣□▣
        |□▣▣""".stripMargin
    val p = new Percolation(3)
    p.open(1, 2)
    p.open(1, 3)
    p.open(2, 1)
    p.open(2, 3)
    p.open(3, 2)
    p.open(3, 3)

    "should has 6 openned sites" in {
      p.numberOfOpenSites() shouldBe 6
    }

    "sould be percolated" in {
      p.percolates() shouldBe true
    }

    "should return correct neighbors" in {
      val neighbors = PrivateMethod[Array[Int]](Symbol("neighbors"))
      val cases = Table(
        ("Row, Col, Neighboars"),
        (1, 1, Array(1, 3)),
        (2, 1, Array(0, 4, 6)),
        (2, 2, Array(1, 3, 5, 7)),
        (3, 3, Array(5, 7))
      )
      forAll(cases) { case (r, c, expected) =>
        p invokePrivate neighbors(r, c) shouldBe expected
      }
    }

    "check openned sites" in {
      val openned = Set(
        1 -> 2,
        1 -> 3,
        2 -> 1,
        2 -> 3,
        3 -> 2,
        3 -> 3
      )
      (1 to 3).foreach(row =>
        (1 to 3).foreach { col =>
          withClue(s"Row $row, Col $col\n$sites\n") {
            p.isOpen(row, col) shouldBe openned.contains(row -> col)
          }
        }
      )
    }

    "check full sites" in {
      val full = Set(
        1 -> 2,
        1 -> 3,
        2 -> 3,
        3 -> 2,
        3 -> 3
      )
      (1 to 3).foreach(row =>
        (1 to 3).foreach { col =>
          withClue(s"Row $row, Col $col\n$sites\n") {
            p.isFull(row, col) shouldBe full.contains(row -> col)
          }
        }
      )
    }
  }

  "test case 2" - {

    val sites =
      """□▣▣
        |▣□□
        |▣▣▣""".stripMargin
    val p = new Percolation(3)
    p.open(1, 2)
    p.open(1, 3)
    p.open(2, 1)
    p.open(3, 1)
    p.open(3, 2)
    p.open(3, 3)

    "should has 6 openned sites" in {
      p.numberOfOpenSites() shouldBe 6
    }

    "sould be percolated" in {
      p.percolates() shouldBe false
    }

    "check openned sites" in {
      val openned = Set(
        1 -> 2,
        1 -> 3,
        2 -> 1,
        3 -> 1,
        3 -> 2,
        3 -> 3
      )
      (1 to 3).foreach(row =>
        (1 to 3).foreach { col =>
          withClue(s"Row $row, Col $col\n$sites\n") {
            p.isOpen(row, col) shouldBe openned.contains(row -> col)
          }
        }
      )
    }

    "check full sites" in {
      val full = Set(
        1 -> 2,
        1 -> 3
      )
      (1 to 3).foreach(row =>
        (1 to 3).foreach { col =>
          withClue(s"Row $row, Col $col\n$sites\n") {
            p.isFull(row, col) shouldBe full.contains(row -> col)
          }
        }
      )
    }
  }
}
