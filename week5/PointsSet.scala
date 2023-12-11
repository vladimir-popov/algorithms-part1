import edu.princeton.cs.algs4.Point2D
import edu.princeton.cs.algs4.RectHV

/** Used for tests unification.
  */
trait PointsSet {

  def name: String

  /** is the set empty?
    */
  def isEmpty(): Boolean

  /** number of points in the set
    */
  def size(): Int

  /** add the point to the set (if it is not already in the set)
    */
  def insert(p: Point2D): Unit

  /** does the set contain point p?
    */
  def contains(p: Point2D): Boolean

  /** draw all points to standard draw
    */
  def draw(): Unit

  /** all points that are inside the rectangle (or on the boundary)
    */
  def range(rect: RectHV): java.lang.Iterable[Point2D]

  /** a nearest neighbor in the set to point p; null if the set is empty
    */
  def nearest(p: Point2D): Point2D
}
