import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.prop.TableFor1
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

import java.lang.Integer
import java.util.Comparator
import scala.util.Random

class MergeSortTest
    extends AnyFreeSpec
    with Matchers
    with TableDrivenPropertyChecks
    with ScalaCheckPropertyChecks {

  implicit val intArbitrary: Arbitrary[Integer] = Arbitrary(
    Gen.choose(0, 100).map(Integer.valueOf)
  )

  val icmp = new Comparator[Integer] {
    override def compare(o1: Integer, o2: Integer): Int =
      Integer.compare(o1, o2)
  }

  "Merging with smaller auxiliary array" in {
    val n = 3
    val arr: Array[Integer] = Array(4, 5, 6, 1, 2, 3)
    MergeSort.merge(arr, icmp, 0, n, 5)
    arr shouldBe sorted
  }

  "should sort test cases" in {
    val cases: TableFor1[Array[Integer]] =
      Table(
        "Array",
        Array[Integer](),
        Array[Integer](1),
        Array[Integer](2, 1),
        Array[Integer](2, 1, 3),
        Array[Integer](2, 1, 4, 3),
        Array[Integer](3, 4, 5, 1, 2)
      )
    forAll(cases) { arr =>
      MergeSort.sort(arr, icmp)
      arr shouldBe sorted
    }
  }

  "should sort arbitrary array" in {
    forAll { (arr: Array[Integer]) =>
      MergeSort.sort(arr, icmp)
      arr shouldBe sorted
    }
  }
}
