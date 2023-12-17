import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

	private static class Node {
		Point2D value;
		Node parent;
		Node rightOrTop;
		Node leftOrBottom;
		boolean isVertical;

		Node(Point2D p) {
			this.value = p;
			this.isVertical = true;
		}

		@Override
		public String toString() {
			return ((isVertical) ? "VERT " : "HOR ") + value;
		}

		static Node insert(Node self, Node n) {
			validateNotNull(n);

			if (self == null)
				return n;

			if (self.value.equals(n.value))
				return n;

			n.parent = self;
			n.isVertical = !self.isVertical;
			Node result;
			if (isBigger(self, n.value)) {
				self.rightOrTop = Node.insert(self.rightOrTop, n);
			} else {
				self.leftOrBottom = Node.insert(self.leftOrBottom, n);
			}
			return self;
		}

		static boolean contains(Node n, Point2D p) {
			if (n == null)
				return false;

			if (n.value.equals(p)) {
				return true;
			}
			if (isBigger(n, p)) {
				return Node.contains(n.rightOrTop, p);
			} else {
				return Node.contains(n.leftOrBottom, p);
			}
		}

		static Point2D nearest(Node n, Point2D p) {
			if (n == null) {
				return null;
			}
			Point2D left = Node.nearest(n.leftOrBottom, p);
			Point2D res = closest(n.value, left, p);

			double rightDistance = (n.isVertical) ? Math.abs(p.x() - n.value.x()) : Math.abs(p.y() - n.value.y());
			if (res.distanceTo(p) < rightDistance)
				return res;

			Point2D right = Node.nearest(n.rightOrTop, p);
			return closest(res, right, p);
		}

		private static Point2D closest(Point2D x, Point2D y, Point2D p) {
			if (y == null)
				return x;
			return (x.distanceTo(p) < y.distanceTo(p)) ? x : y;
		}

		static Iterable<Point2D> range(Node n, RectHV rect, LinkedList<Point2D> accum) {
			if (n == null)
				return accum;

			if (rect.contains(n.value)) {
				accum.add(n.value);
			}
			if (isLesssOrEqual(n, rect.xmin(), rect.ymin())) {
				Node.range(n.leftOrBottom, rect, accum);
			}
			if (isBiggerOrEqual(n, rect.xmax(), rect.ymax())) {
				Node.range(n.rightOrTop, rect, accum);
			}
			return accum;
		}

		/** @return true if the point is right or up to the node */
		private static boolean isBigger(Node n, Point2D p) {
			return (n.isVertical) ? p.x() > n.value.x() : p.y() > n.value.y();
		}

		private static boolean isBiggerOrEqual(Node n, double x, double y) {
			return (n.isVertical) ? x >= n.value.x() : y >= n.value.y();
		}

		private static boolean isLesssOrEqual(Node n, double x, double y) {
			return (n.isVertical) ? x <= n.value.x() : y <= n.value.y();
		}

		static void draw(Node n, int depth, String side) {
			if (n == null)
				return;

			System.out.println(side + " " + n + " : " + depth);

			java.awt.Color penColor = StdDraw.getPenColor();
			double penRadius = StdDraw.getPenRadius();
			StdDraw.text(n.value.x(), n.value.y() - 0.03, n.value.toString());
			StdDraw.setPenRadius(penRadius / 2);
			if (n.isVertical) {
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(n.value.x(), 0.0, n.value.x(), 1.0);
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(0.0, n.value.y(), 1.0, n.value.y());
			}
			StdDraw.setPenRadius(2 * penRadius);
			n.value.draw();
			StdDraw.setPenColor(penColor);
			StdDraw.setPenRadius(penRadius);

			Node.draw(n.leftOrBottom, depth + 1, "Left");
			Node.draw(n.rightOrTop, depth + 1, "Right");
		}
	}

	private static void validateNotNull(Object obj) {
		if (obj == null)
			throw new IllegalArgumentException();
	}

	private Node root;
	private int count;

	/**
	 * construct an empty set of points
	 */
	public KdTree() {
		this.root = null;
		this.count = 0;
	}

	/**
	 * is the set empty?
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * number of points in the set
	 */
	public int size() {
		return count;
	}

	/**
	 * add the point to the set (if it is not already in the set)
	 */
	public void insert(Point2D p) {
		validateNotNull(p);
		if (contains(p))
			return;

		this.root = Node.insert(this.root, new Node(p));
		this.count++;
	}

	/**
	 * does the set contain point p?
	 */
	public boolean contains(Point2D p) {
		validateNotNull(p);
		return Node.contains(root, p);
	}

	/**
	 * draw all points to standard draw
	 */
	public void draw() {
		Node.draw(this.root, 0, "Root");
	}

	/**
	 * all points that are inside the rectangle (or on the boundary)
	 */
	public Iterable<Point2D> range(RectHV rect) {
		validateNotNull(rect);
		LinkedList<Point2D> accum = new LinkedList<Point2D>();
		return Node.range(root, rect, accum);
	}

	/**
	 * a nearest neighbor in the set to point p; null if the set is empty
	 */
	public Point2D nearest(Point2D p) {
		validateNotNull(p);
		return Node.nearest(root, p);
	}
}
