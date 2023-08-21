public class FindSuccessor {

	private int[] xs;

	public FindSuccessor(int n) {
		xs = new int[n];
		for (int i = 0; i < n; i++) {
			xs[i] = i;
		}
	}

	public void delete(int i) {
		if (i == xs.length - 1)
			xs[i] = -1;
		else
			xs[i] = xs[i + 1];
	}

	public void deleteAll(int[] is) {
		for (int i : is) {
			delete(i);
		}
	}

	public int get(int i) {
		if (i == xs[i] || i == xs.length - 1)
			return xs[i];
		else
			return get(xs[i]);
	}
}
