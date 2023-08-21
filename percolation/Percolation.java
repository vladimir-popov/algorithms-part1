import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int N;
	private int O;
	private WeightedQuickUnionUF uf;
	private boolean[] opened;
	private int openedCount = 0;

	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		N = n;
		O = N * N;
		opened = new boolean[O];
		// N - full sites
		uf = new WeightedQuickUnionUF(O + 1);
	}

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		int p = p(row, col);

		if (opened[p])
			return;

		opened[p] = true;
		openedCount++;
		if (row == 1)
			uf.union(O, p);

		for (int n : neighbors(row, col)) {
			if (opened[n])
				uf.union(p, n);

		}
	}

	// is the site (row, col) open?
	public boolean isOpen(int row, int col) {
		return opened[p(row, col)];
	}

	// is the site (row, col) full?
	public boolean isFull(int row, int col) {
		int p = p(row, col);
		if (opened[p])
			return uf.find(O) == uf.find(p);
		else
			return false;
	}

	// returns the number of open sites
	public int numberOfOpenSites() {
		return openedCount;
	}

	// does the system percolate?
	public boolean percolates() {
		int o = uf.find(O);
		for (int i = N * (N - 1); i < O; i++) {
			if (opened[i] && uf.find(i) == o)
				return true;
		}
		return false;
	}

	private int p(int row, int col) {
		if (isNotValid(row) || isNotValid(col))
			throw new IllegalArgumentException();
		return N * (row - 1) + col - 1;
	}

	private int[] neighbors(int row, int col) {
		int[] res = new int[hight(row) - low(row) + hight(col) - low(col)];
		int n = 0;
		for (int i = low(row); i <= hight(row); i++) {
			for (int j = low(col); j <= hight(col); j++) {
				if ((i == row && j == col) || (i != row && j != col))
					continue;
				res[n] = p(i, j);
				n++;
			}
		}
		return res;
	}

	private int low(int x) {
		return isValid(x - 1) ? x - 1 : x;
	}

	private int hight(int x) {
		return isValid(x + 1) ? x + 1 : x;
	}

	private boolean isValid(int x) {
		return !isNotValid(x);
	}

	private boolean isNotValid(int x) {
		return x < 1 || x > N;
	}
}
