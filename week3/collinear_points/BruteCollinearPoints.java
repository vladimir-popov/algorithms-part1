import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

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

		segments = new LineSegment[points.length];

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
						Point[] ps = { points[i], points[j], points[l], points[k] };
						Arrays.sort(ps);
						segments[segmentsCount++] = new LineSegment(ps[0], ps[3]);
					}
				}
			}
		}
	}

	/* the number of line segments */
	public int numberOfSegments() {
		return segmentsCount;
	}

	/* the line segments */
	public LineSegment[] segments() {
		return Arrays.copyOf(segments, segmentsCount);
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
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		int i = 0;
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();
	}
}
