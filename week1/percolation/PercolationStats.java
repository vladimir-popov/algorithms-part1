import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {

	private int N;
	private int T;
	private double[] xs;
	private double _mean;
	private double _stddev;
	private double _lo;
	private double _hi;

	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {
		if (n <= 0 || trials <= 0)
			throw new IllegalArgumentException();
		N = n;
		T = trials;
		xs = new double[T];
		for (int i = 0; i < T; i++) {
			xs[i] = threshold(N);
		}
		_mean = StdStats.mean(xs);
		_stddev = StdStats.stddev(xs);
		double _t = Math.sqrt(T);
		_lo = _mean - 1.96 * _stddev / _t;
		_hi = _mean + 1.96 * _stddev / _t;
	}

	// sample mean of percolation threshold
	public double mean() {
		return _mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return _stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return _lo;
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return _hi;
	}

	// takes two command-line arguments n and T,
	// performs T independent computational experiments on an n-by-n grid,
	// and prints the sample mean, sample standard deviation,
	// and the 95% confidence interval for the percolation threshold.
	public static void main(String[] args) {
		var n = Integer.parseInt(args[0]);
		var t = Integer.parseInt(args[1]);
		var p = new PercolationStats(n, t);
		StdOut.print("mean                    = ");
		StdOut.println(p.mean());
		StdOut.print("stddev                  = ");
		StdOut.println(p.stddev());
		StdOut.printf("95%% confidence interval = [%f, %f]", p.confidenceLo(), p.confidenceHi());
	}

	private double threshold(int N) {
		Percolation p = new Percolation(N);
		while (!p.percolates()) {
			int row = StdRandom.uniformInt(N) + 1;
			int col = StdRandom.uniformInt(N) + 1;
			p.open(row, col);
		}
		return p.numberOfOpenSites() / (double) (N * N);
	}
}
