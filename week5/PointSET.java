import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.LinkedList;

public class PointSET {

	private TreeSet<Point2D> points;

	/**
	 * construct an empty set of points
	 */
	public PointSET() {
		this.points = new TreeSet<Point2D>();
	}

	/**
	 * is the set empty?
	 */
	public boolean isEmpty() {
		return points.isEmpty();
	}

	/**
	 * number of points in the set
	 */
	public int size() {
		return points.size();
	}

	/**
	 * add the point to the set (if it is not already in the set)
	 */
	public void insert(Point2D p) {
		validateNotNull(p);
		points.add(p);
	}

	/**
	 * does the set contain point p?
	 */
	public boolean contains(Point2D p) {
		validateNotNull(p);
		return points.contains(p);
	}

	/**
	 * draw all points to standard draw
	 */
	public void draw() {
		for (Point2D p : points) {
			p.draw();
		}
	}

	/**
	 * all points that are inside the rectangle (or on the boundary)
	 */
	public Iterable<Point2D> range(RectHV rect) {
		validateNotNull(rect);
		LinkedList<Point2D> result = new LinkedList<>();
		for (Point2D p : points) {
			if (rect.contains(p))
				result.add(p);
		}
		return result;
	}

	/**
	 * a nearest neighbor in the set to point p; null if the set is empty
	 */
	public Point2D nearest(Point2D point) {
		validateNotNull(point);
		if (isEmpty())
			return null;

		double min = Double.MAX_VALUE;
		Point2D result = null;
		for (Point2D p : points) {
			double distance = p.distanceTo(point);
			if (distance < min) {
				result = p;
				min = distance;
			}
		}
		return result;
	}

	private void validateNotNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException();
	}
}
