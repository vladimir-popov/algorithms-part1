import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.PrivateMethodTester
import java.util.Comparator
import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher
import org.scalatest.matchers.BeMatcher

class RBTreeTest extends AnyFreeSpec with Matchers with PrivateMethodTester {
  val cmp: Comparator[Char] = new Comparator[Char] {
    override def compare(x: Char, y: Char): Int =
      Character.compare(x, y)
  }
  "Rotate left" in {
    // given:
    val tree: RBTree[Char] =
      //   e
      //     ^s
      //    g^
      new RBTree[Char](
        'e',
        cmp,
        null,
        new RBTree('s', cmp, new RBTree('g', cmp), null)
      )
    tree.right.left.color = RBTree.Color.BLACK

    // when:
    val result: RBTree[Char] = RBTree.rotateLeft(tree)

    // then:
    result.value shouldBe 's'
    result.left.value shouldBe 'e'
    result.left.color shouldBe RBTree.Color.RED
    result.left.right.value shouldBe 'g'
    result.left.right.color shouldBe RBTree.Color.BLACK
  }

  "Rotate right" in {
    // given:
    val tree: RBTree[Char] =
      //         s
      //      e^
      //   a^   ^g
      new RBTree(
        's',
        cmp,
        new RBTree('e', cmp, new RBTree('a', cmp), new RBTree('g', cmp)),
        null
      )
    tree.left.color = RBTree.Color.RED
    tree.left.left.color = RBTree.Color.RED

    // when:
    val result = RBTree.rotateRight(tree)

    // then:
    result.value shouldBe 'e'
    result.left.value shouldBe 'a'
    result.right.value shouldBe 's'
    result.left.color shouldBe RBTree.Color.RED
    result.right.left.value shouldBe 'g'
  }

  "Change color" in {
    // given:
    val tree = new RBTree('e', cmp, new RBTree('a', cmp), new RBTree('s', cmp))
    tree.left.color = RBTree.Color.RED
    tree.right.color = RBTree.Color.RED

    // when:
    val result = RBTree.changeColor(tree)

    // then:
    result.color shouldBe RBTree.Color.RED
    result.left.color shouldBe RBTree.Color.BLACK
    result.right.color shouldBe RBTree.Color.BLACK
  }

  "Insert in the tree with size 2" - {
    def validate(tree: RBTree[Char]) = {
      tree shouldBe red('b')
      tree.left shouldBe black('a')
      tree.right shouldBe black('c')
    }

    "Insert C" in {
      // given:
      val tree = new RBTree('b', cmp, new RBTree('a', cmp), null)
      // when:
      val result = tree.add('c')
      // then:
      validate(result)
    }

    "Insert A" in {
      // given:
      val tree = new RBTree('c', cmp, new RBTree('b', cmp), null)
      // when:
      val result = tree.add('a')
      // then:
      validate(result)
    }

    "Insert B" in {
      // given:
      val tree = new RBTree('c', cmp, new RBTree('a', cmp), null)
      // when:
      val result = tree.add('b')
      // then:
      validate(result)
    }
  }

  "Test cases" - {
    "Insert P" in {
      // given:
      val c =
        new RBTree('c', cmp, new RBTree('a', cmp), null, RBTree.Color.BLACK)
      val m =
        new RBTree('m', cmp, new RBTree('h', cmp), null, RBTree.Color.BLACK)
      val e = new RBTree('e', cmp, c, m, RBTree.Color.BLACK)
      val tree = new RBTree('r', cmp, e, new RBTree('s', cmp))
      tree.right.color = RBTree.Color.BLACK

      // when:
      val result = tree.add('p')

      // then:
      result shouldBe red('m')
      result.left shouldBe black('e')
      result.left.right shouldBe black('h')
      result.left.left shouldBe black('c')
      result.left.left.left shouldBe red('a')
      result.right shouldBe black('r')
      result.right.left shouldBe black('p')
      result.right.right shouldBe black('s')
    }
  }

  private def value(value: Char): Matcher[RBTree[Char]] =
    (left: RBTree[Char]) =>
      MatchResult(
        left.value == value,
        s"""Wrong value of the node. Expected $value, but was ${left.value}""",
        ""
      )

  private def color(color: RBTree.Color): Matcher[RBTree[Char]] =
    (left: RBTree[Char]) =>
      MatchResult(
        left.color == color,
        s"""Wrong color of the node ${left.value}. Expected $color, but was ${left.color}""",
        ""
      )

  private def black(v: Char): BeMatcher[RBTree[Char]] =
    (value(v) and color(RBTree.Color.BLACK))(_)

  private def red(v: Char): BeMatcher[RBTree[Char]] =
    (value(v) and color(RBTree.Color.RED))(_)
}
