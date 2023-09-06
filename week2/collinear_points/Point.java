import java.lang.Integer;
import java.lang.Double;
import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

	public final int x;
	public final int y;

	/** constructs the point (x, y) */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/* draws this point */
	public void draw() {
		StdDraw.point(x, y);
	}

	/* draws the line segment from this point to that point */
	public void drawTo(Point that) {
		StdDraw.line(this.x, this.y, that.x, that.y);
	}

	/* string representation */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	/* compare two points by y-coordinates, breaking ties by x-coordinates */
	public int compareTo(Point that) {
		if (this.y == that.y)
			return Integer.compare(this.x, that.x);
		else
			return Integer.compare(this.y, that.y);
	}

	/* the slope between this point and that point */
	public double slopeTo(Point that) {
		if (compareTo(that) == 0)
			return Double.NEGATIVE_INFINITY;
		if (this.y == that.y)
			return 0.0;
		if (this.x == that.x)
			return Double.POSITIVE_INFINITY;

		return ((double) that.y - this.y) / (that.x - this.x);
	}

	/* compare two points by slopes they make with this point */
	public Comparator<Point> slopeOrder() {
		Point p0 = this;
		return new Comparator<Point>() {
			public int compare(Point p1, Point p2) {
				return Double.compare(p0.slopeTo(p1), p0.slopeTo(p2));
			}
		};
	}
}
