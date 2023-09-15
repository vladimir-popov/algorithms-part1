import java.util.Comparator;

public class BinaryHeap<T> {

	private T[] nodes;
	private int count = 0;
	private Comparator<T> cmp;

	public BinaryHeap(int capacity, Comparator<T> cmp) {
		this.nodes = (T[]) new Object[capacity + 1];
		this.cmp = cmp;
	}

	public void add(T value) {
		this.nodes[++count] = value;
		swim(count);
	}

	public T removeMax() {
		T max = nodes[1];
		swap(1, count--);
		sink(1);
		return max;
	}

	public int size() {
		return count;
	}

	private void swim(int k) {
		while (k > 1 && less(k / 2, k)) {
			swap(k, k / 2);
			k = k / 2;
		}
	}

	private void sink(int k) {
		while (2 * k < (count - 1) && (less(k, 2 * k) || less(k, 2 * k + 1))) {
			int c = less(2 * k, 2 * k + 1) ? 2 * k + 1 : 2 * k;
			swap(k, c);
			k = c;
		}
	}

	private void swap(int a, int b) {
		T tmp = nodes[a];
		nodes[a] = nodes[b];
		nodes[b] = tmp;
	}

	private boolean less(int a, int b) {
		return cmp.compare(nodes[a], nodes[b]) < 0;
	}
}
