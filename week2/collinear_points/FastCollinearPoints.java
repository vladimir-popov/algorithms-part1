import java.util.Arrays;
import java.util.Comparator;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class FastCollinearPoints {
	private int segmentsCount = 0;
	private LineSegment[] segments = new LineSegment[0];

	/** finds all line segments containing 4 or more points */
	public FastCollinearPoints(Point[] points) {
		if (points == null)
			throw new IllegalArgumentException();

		segments = new LineSegment[points.length];
		WeightedQuickUnionUF union = new WeightedQuickUnionUF(points.length);

		class Tuple {
			public int idx;
			public double slope;

			public Tuple(int idx, double slope) {
				this.idx = idx;
				this.slope = slope;
			}

			public Point p() {
				return points[idx];
			}

			public String toString() {
				return p().toString() + slope;
			}
		}

		Comparator<Tuple> cp = new Comparator<Tuple>() {
			public int compare(Tuple x, Tuple y) {
				if (x.slope == y.slope)
					return x.p().compareTo(y.p());
				else
					return Double.compare(x.slope, y.slope);
			}
		};

		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				throw new IllegalArgumentException();

			Tuple[] tuples = new Tuple[points.length - i - 1];
			for (int j = i + 1; j < points.length; j++) {
				if (points[j] == null || points[i].compareTo(points[j]) == 0)
					throw new IllegalArgumentException();

				tuples[j - i - 1] = new Tuple(j, points[i].slopeTo(points[j]));
			}
			Arrays.sort(tuples, cp);

			int s = 0;
			int e = 0;
			while (e < tuples.length) {
				e++;
				if (e >= tuples.length || tuples[s].slope != tuples[e].slope) {
					if (e - s > 2) {
						int idx1 = min(points, i, tuples[s].idx);
						int idx2 = max(points, i, tuples[e - 1].idx);
						if (union.find(idx2) == union.find(idx1))
							continue;
						for (int x = s; x < e; x++)
							union.union(i, tuples[x].idx);
						LineSegment ls = new LineSegment(points[idx1], points[idx2]);
						segments[segmentsCount++] = ls;
					}
					s = e;
				}
			}
		}
		this.segments = Arrays.copyOf(this.segments, segmentsCount);
	}

	private int min(Point[] points, int idx1, int idx2) {
		Point x = points[idx1];
		Point y = points[idx2];
		return (x.compareTo(y) <= 0) ? idx1 : idx2;
	}

	private int max(Point[] points, int idx1, int idx2) {
		Point x = points[idx1];
		Point y = points[idx2];
		return (x.compareTo(y) >= 0) ? idx1 : idx2;
	}

	/** the number of line segments */
	public int numberOfSegments() {
		return segmentsCount;
	}

	/** the line segments */
	public LineSegment[] segments() {
		return segments;
	}
}
