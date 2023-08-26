import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int N;
	/** Number of the group for full sites */
	private int F;
	/** Number of the group for sites on the last row */
	private int L;
	private WeightedQuickUnionUF uf;
	private boolean[] opened;
	private int openedCount = 0;

	// creates n-by-n grid, with all sites initially blocked
	public Percolation(int n) {
		if (n <= 0)
			throw new IllegalArgumentException();
		N = n;
		F = N * N;
		L = F + 1;
		opened = new boolean[F];
		// N - full sites
		uf = new WeightedQuickUnionUF(N * N + 2);
	}

	// opens the site (row, col) if it is not open already
	public void open(int row, int col) {
		int p = p(row, col);

		if (opened[p])
			return;

		opened[p] = true;
		openedCount++;
		if (row == 1)
			uf.union(F, p);

		if (row == N)
			uf.union(L, p);

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
			return uf.find(F) == uf.find(p);
		else
			return false;
	}

	// returns the number of open sites
	public int numberOfOpenSites() {
		return openedCount;
	}

	// does the system percolate?
	public boolean percolates() {
		return uf.find(F) == uf.find(L);
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
