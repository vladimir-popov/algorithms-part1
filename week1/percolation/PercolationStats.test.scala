import edu.princeton.cs.algs4.StdRandom
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers

import java.io.ByteArrayOutputStream
import java.io.PrintStream
import scala.util.Success
import scala.util.Try
import scala.util.Using

class PercolationStatsTest extends AnyFreeSpec with Matchers {

  "Constructor" - {
    "should throw IllegalArgumentException when n <= 0" in {
      assertThrows[IllegalArgumentException] {
        new PercolationStats(-1, 1)
      }
      assertThrows[IllegalArgumentException] {
        new PercolationStats(0, 1)
      }
    }
    "should throw IllegalArgumentException when trials <= 0" in {
      assertThrows[IllegalArgumentException] {
        new PercolationStats(1, -1)
      }
      assertThrows[IllegalArgumentException] {
        new PercolationStats(1, 0)
      }
    }
    "measure time" ignore {
      import edu.princeton.cs.algs4.Stopwatch
      def measure(f: => Any) = {
        val s = new Stopwatch()
        f
        s.elapsedTime()
      }

      println("Measure trails for 50x50:")
      Range(1, 1001, 50)
        .map(x => x -> measure(new PercolationStats(50, x)))
        .foreach { case (count, time) =>
          println(s"$count, $time")
        }

      println("\nMeasure Ns with 100 trials:")
      Range(1, 301, 30)
        .map(x => x -> measure(new PercolationStats(x, 100)))
        .foreach { case (count, time) =>
          println(s"$count, $time")
        }
    }
  }

  "Test cases" - {
    testCases.zipWithIndex.foreach {
      case (TestCase(n, trials, mean, stddev, Array(lo, hi)), idx) =>
        s"Test case $idx" in {
          val stats = new PercolationStats(n, trials)
          withClue("mean") {
            stats.mean() should be(mean +- 0.01)
          }
          withClue("stddev") {
            stats.stddev() should be(stddev +- 0.01)
          }
          withClue("confidence interval") {
            stats.confidenceLo() should be(lo +- 0.01)
            stats.confidenceHi() should be(hi +- 0.01)
          }
        }
    }
  }

  "Main" - {
    "should take two command-line arguments n and T, and print result" in {
      val expectations = Seq(
        "mean                    = 0\\.\\d+".r,
        "stddev                  = 0\\.\\d+".r,
        "95% confidence interval = \\[0\\.\\d+, 0\\.\\d+\\]".r
      )
      StdRandom.setSeed(100500L)
      val output = sysOutToStr {
        PercolationStats.main(Array("100", "200"))
      }.get
      val lines = output.linesIterator
      withClue(output) {
        lines should have size 3
      }
      expectations.zip(lines).foreach { case (expectation, line) =>
        withClue(line) {
          expectation.matches(line) shouldBe true
        }
      }
    }
  }

  case class TestCase(
      n: Int,
      trials: Int,
      mean: Double,
      stddev: Double,
      confidenceInterval: Array[Double]
  )

  lazy val testCases = Seq(
    TestCase(
      n = 200,
      trials = 100,
      mean = 0.5929934999999997,
      stddev = 0.00876990421552567,
      confidenceInterval = Array(0.5912745987737567, 0.5947124012262428)
    ),
    TestCase(
      n = 200,
      trials = 100,
      mean = 0.592877,
      stddev = 0.009990523717073799,
      confidenceInterval = Array(0.5909188573514536, 0.5948351426485464)
    ),
    TestCase(
      n = 2,
      trials = 10000,
      mean = 0.666925,
      stddev = 0.11776536521033558,
      confidenceInterval = Array(0.6646167988418774, 0.6692332011581226)
    ),
    TestCase(
      n = 2,
      trials = 100000,
      mean = 0.6669475,
      stddev = 0.11775205263262094,
      confidenceInterval = Array(0.666217665216461, 0.6676773347835391)
    )
  )

  private def sysOutToStr(f: => Any): Try[String] = {
    val orig = System.out
    val result = Using(new ByteArrayOutputStream()) { baos =>
      val ps = new PrintStream(baos)
      System.setOut(ps)
      f
      System.out.flush()
      new String(baos.toByteArray())
    }
    System.setOut(orig)
    result
  }
}
