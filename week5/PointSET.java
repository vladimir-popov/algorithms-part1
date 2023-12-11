import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Iterator;
import java.util.TreeSet;

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
		var sm = points.stream().filter(p -> rect.contains(p));
		return (Iterable<Point2D>) () -> sm.iterator();
	}

	/**
	 * a nearest neighbor in the set to point p; null if the set is empty
	 */
	public Point2D nearest(Point2D point) {
		validateNotNull(point);
		if (isEmpty())
			return null;

		Iterator<Point2D> itr = points.iterator();
		Point2D result = itr.next();
		double distance = Double.MAX_VALUE;
		while (itr.hasNext()) {
			Point2D p = itr.next();
			double d = point.distanceTo(p);
			if (d < distance) {
				distance = d;
				result = p;
			}
		}
		return result;
	}

	private void validateNotNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException();
	}
}
