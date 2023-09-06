import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

	private int segmentsCount = 0;
	private LineSegment[] segments;

	/* finds all line segments containing 4 points */
	public BruteCollinearPoints(Point[] points) {
		if (points == null)
			throw new IllegalArgumentException();
		for (int i = 0; i < points.length; i++)
			if (points[i] == null)
				throw new IllegalArgumentException();

		LineSegment[] segments = new LineSegment[points.length];

		for (int i = 0; i < points.length; i++) {
			for (int j = i + 1; j < points.length; j++) {
				double sj = points[i].slopeTo(points[j]);
				if (sj == Double.NEGATIVE_INFINITY)
					throw new IllegalArgumentException("Equal points found.");
				for (int l = j + 1; l < points.length; l++) {
					double sl = points[j].slopeTo(points[l]);
					if (sl != sj)
						continue;
					for (int k = l + 1; k < points.length; k++) {
						double sk = points[l].slopeTo(points[k]);
						if (sl != sk)
							continue;
						segments[segmentsCount++] = new LineSegment(points[i], points[k]);
					}
				}
			}
		}

		this.segments = Arrays.copyOf(segments, segmentsCount);
	}

	// xs - sorted array of 4
	private boolean isCollinear(Point[] xs) {
		Comparator<Point> cp = xs[0].slopeOrder();
		return (cp.compare(xs[1], xs[2]) + cp.compare(xs[2], xs[3])) == 0;
	}

	/* the number of line segments */
	public int numberOfSegments() {
		return segmentsCount;
	}

	/* the line segments */
	public LineSegment[] segments() {
		return segments;
	}

	public static void main(String[] args) {

		// read the n points from a file
		In in = new In(args[0]);
		int n = in.readInt();
		Point[] points = new Point[n];
		for (int i = 0; i < n; i++) {
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}

		StdDraw.setPenRadius(0.01);

		// draw the points
		StdDraw.enableDoubleBuffering();
		// StdDraw.setXscale(0, 32768);
		// StdDraw.setYscale(0, 32768);
		int i = 0;
		for (Point p : points) {
			StdDraw.point(p.x * 0.1, p.y * 0.1);
			StdDraw.text(p.x * 0.1, (p.y + 1) * 0.1, p.toString() + i++);
		}
		StdDraw.show();

		// print and draw the line segments
		// BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		// for (LineSegment segment : collinear.segments()) {
		// StdOut.println(segment);
		// segment.draw();
		// }
		// StdDraw.show();
	}
}
