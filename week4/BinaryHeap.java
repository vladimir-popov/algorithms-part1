import java.util.Comparator;
import java.util.Arrays;

public class BinaryHeap<T> {

	private T[] nodes;
	private int count = 0;
	private Comparator<T> cmp;

	public static <T> void sort(T[] xs, Comparator<T> cp) {
		BinaryHeap<T> bh = new BinaryHeap<>(xs.length, cp);
		for (int i = 0; i < xs.length; i++) {
			bh.add(xs[i]);
		}
		for (int i = xs.length - 1; i >= 0; i--) {
			xs[i] = bh.removeMax();
		}
	}

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

	public String toString() {
		return Arrays.toString(nodes);
	}

	private void swim(int k) {
		while (k > 1 && less(k / 2, k)) {
			swap(k, k / 2);
			k = k / 2;
		}
	}

	private void sink(int k) {
		while (2 * k <= count) {
			int j = 2 * k;
			if (j < count && less(j, j + 1))
				j++;
			if (less(k, j))
				swap(k, j);
			k = j;
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
