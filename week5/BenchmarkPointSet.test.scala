import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D
import org.scalacheck.rng.Seed
import org.scalacheck.Gen
import org.scalatest.freespec.AnyFreeSpec

class BenchmarkPointSet extends AnyFreeSpec with Utils {
  def readSets(filename: String) = {
    val in = new In(filename)
    val kdTree = createKdTree()
    val brutForce = createBrutForce()
    while (!in.isEmpty()) {
      val x = in.readDouble()
      val y = in.readDouble()
      kdTree.insert(new Point2D(x, y))
      brutForce.insert(new Point2D(x, y))
    }
    brutForce -> kdTree
  }
  def measure(f: PointsSet => Unit)(set: PointsSet) = {
    val start = System.currentTimeMillis()
    val duration = 5000
    var count = 0L
    while (System.currentTimeMillis() - start < duration) {
      f(set)
      count += 1
    }
    info(s"rate is ${1000 * count / duration} op/sec")
  }
  def benchmark(bench: PointsSet => Unit) = {
    "1K points" - {
      val (brutForce, kdTree) = readSets("week5/input1K.txt")
      brutForce.name in bench(brutForce)
      kdTree.name in bench(kdTree)
    }
    "100K points" - {
      val (brutForce, kdTree) = readSets("week5/input100K.txt")
      brutForce.name in bench(brutForce)
      kdTree.name in bench(kdTree)
    }
    "1M points" - {
      val (brutForce, kdTree) = readSets("week5/input1M.txt")
      brutForce.name in bench(brutForce)
      kdTree.name in bench(kdTree)
    }
  }
  "Method nearest" - {
    benchmark(measure {
      _.nearest(
        arbPoint2D.arbitrary
          .pureApply(Gen.Parameters.default, Seed.random())
      )
    })
  }
  "Method range" - {
    benchmark(measure {
      _.range(
        arbRectHV.arbitrary
          .pureApply(Gen.Parameters.default, Seed.random())
      )
    })
  }
}
