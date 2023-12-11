import java.util.Comparator;

public class RBTree<A> {
	public static enum Color {
		RED, BLACK
	}

	public static <A> RBTree<A> add(RBTree<A> self, RBTree<A> node) {
		if (self == null)
			return node;

		int c = self.cmp.compare(self.value, node.value);
		if (c < 0)
			self.right = RBTree.add(self.right, node);
		if (c > 0)
			self.left = RBTree.add(self.left, node);

		if (isRed(self.right))
			self = rotateLeft(self);
		if (isRed(self.left) && isRed(self.left.left))
			self = rotateRight(self);
		if (isRed(self.left) && isRed(self.right))
			self = changeColor(self);

		return self;
	}

	static <A> boolean isRed(RBTree<A> tree) {
		return tree != null && tree.color == Color.RED;
	}

	static <A> RBTree<A> rotateLeft(RBTree<A> node) {
		RBTree x = node.right;
		node.right = x.left;
		x.left = node;
		x.parent = node.parent;
		node.parent = x;
		node.color = RBTree.Color.RED;
		return x;
	}

	static <A> RBTree<A> rotateRight(RBTree<A> node) {
		RBTree<A> x = node.left;
		node.left = x.right;
		x.right = node;
		x.parent = node.parent;
		node.parent = x;
		node.color = RBTree.Color.RED;
		return x;
	}

	static <A> RBTree<A> changeColor(RBTree<A> node) {
		node.color = RBTree.Color.RED;
		node.left.color = RBTree.Color.BLACK;
		node.right.color = RBTree.Color.BLACK;
		return node;
	}

	public final A value;

	public RBTree(A a, Comparator<A> cmp) {
		this(a, cmp, null, null);
	}

	public RBTree<A> add(A a) {
		return RBTree.add(this, new RBTree(a, cmp));
	}

	RBTree<A> parent;
	Color color;
	RBTree<A> left;
	RBTree<A> right;
	private final Comparator<A> cmp;

	RBTree(A a, Comparator<A> cmp, RBTree<A> left, RBTree<A> right) {
		this(a, cmp, left, right, Color.RED);
	}

	RBTree(A a, Comparator<A> cmp, RBTree<A> left, RBTree<A> right, Color color) {
		assert (a != null);
		assert (cmp != null);
		assert (color != null);

		this.cmp = cmp;
		this.value = a;
		this.color = color;
		if (left != null) {
			this.left = left;
			left.parent = this;
		}
		if (right != null) {
			this.right = right;
			right.parent = this;
		}
	}
}
