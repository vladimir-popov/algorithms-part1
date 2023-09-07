import java.util.Comparator;

public class MergeSort {

	public static <T> void sort(T[] xs, Comparator<T> c) {
		sort(xs, c, 0, xs.length - 1);
	}

	private static <T> void sort(T[] xs, Comparator<T> c, int lo, int hi) {
		int length = hi - lo + 1;
		if (length < 2)
			return;
		if (length == 2) {
			if (less(xs[hi], xs[lo], c))
				swap(xs, hi, lo);
			return;
		} else {
			int mid = lo + length / 2;
			sort(xs, c, lo, mid);
			sort(xs, c, mid + 1, hi);
			merge(xs, c, lo, mid + 1, hi);
		}
	}

	static <T> void merge(T[] xs, Comparator<T> c, int lo, int mid, int hi) {
		int length = hi - lo + 1;
		if (length < 2)
			return;
		T[] aux = (T[]) new Object[length];
		copy(xs, aux, lo, 0, length);

		int m = mid - lo;
		int i = 0;
		int j = m;
		int k = lo;
		while (i < m && j < length) {
			if (less(aux[j], aux[i], c))
				xs[k++] = aux[j++];
			else
				xs[k++] = aux[i++];
		}
		if (i < m - 1)
			copy(aux, xs, i, k, m - i);
		if (j < length - 1)
			copy(aux, xs, j, k, length - j);
	}

	private static <T> boolean less(T x, T y, Comparator<T> c) {
		return c.compare(x, y) < 0;
	}

	private static <T> void swap(T[] xs, int i, int j) {
		T tmp = xs[i];
		xs[i] = xs[j];
		xs[j] = tmp;
	}

	private static <T> void copy(T[] from, T[] to, int fi, int ti, int count) {
		for (int i = 0; i < count; i++) {
			to[i + ti] = from[i + fi];
		}
	}
}
