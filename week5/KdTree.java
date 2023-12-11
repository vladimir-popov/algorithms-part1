import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

	private static class InsertResult {
		Node node;
		boolean isInserted;

		InsertResult(Node node, boolean isInserted) {
			this.node = node;
			this.isInserted = isInserted;
		}
	}

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

		static InsertResult insert(Node self, Node n) {
			validateNotNull(n);

			if (self == null)
				return new InsertResult(n, true);

			if (self.value.equals(n.value))
				return new InsertResult(self, false);

			n.parent = self;
			n.isVertical = !self.isVertical;
			InsertResult result;
			boolean isBigger = (self.isVertical) ? (self.value.x() < n.value.x()) : (self.value.y() < n.value.y());
			if (isBigger) {
				result = Node.insert(self.rightOrTop, n);
			} else {
				result = Node.insert(self.leftOrBottom, n);
			}

			if (result.isInserted == false)
				return result;

			if (isBigger)
				self.rightOrTop = result.node;
			else
				self.leftOrBottom = result.node;

			return new InsertResult(self, true);
		}

		static boolean contains(Node n, Point2D p) {
			if (n == null)
				return false;

			if (n.value.equals(p)) {
				return true;
			}
			boolean isBigger = (n.isVertical) ? p.x() > n.value.x() : p.y() > n.value.y();
			if (isBigger) {
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

		static Iterable<Point2D> range(Node n, RectHV rect) {
			return null;
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
		InsertResult result = Node.insert(this.root, new Node(p));
		if (result.isInserted) {
			this.root = result.node;
			this.count++;
		}
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
		return null;
	}

	/**
	 * a nearest neighbor in the set to point p; null if the set is empty
	 */
	public Point2D nearest(Point2D p) {
		validateNotNull(p);
		return Node.nearest(root, p);
	}

	public static void main(String[] args) throws Exception {
		StdDraw.setPenRadius(0.01);
		var points = new KdTree();
		points.insert(new Point2D(0.7, 0.2));
		points.insert(new Point2D(0.5, 0.4));
		points.insert(new Point2D(0.2, 0.3));
		points.insert(new Point2D(0.4, 0.7));
		points.insert(new Point2D(0.9, 0.6));
		points.draw();
		System.in.read();
	}
}
