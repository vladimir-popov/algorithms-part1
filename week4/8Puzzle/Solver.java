import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import java.util.Iterator;
import java.util.Comparator;

public class Solver {

	private static class Solution implements Comparable<Solution>, Iterable<Board> {
		public final Board board;
		private final int move;
		private final Solution prev;

		// lazy val
		private int score = -1;

		public Solution(Board board) {
			this.board = board;
			this.move = 0;
			this.prev = null;
		}

		private Solution(Board board, Solution prev) {
			this.board = board;
			this.move = prev.move + 1;
			this.prev = prev;
		}

		public Solution add(Board b) {
			return new Solution(b, this);
		}

		public boolean contains(Board b) {
			Solution current = this;
			while (current != null) {
				if (current.board.equals(b))
					return true;
				current = current.prev;
			}
			return false;
		}

		public int size() {
			return move;
		}

		public int score() {
			if (score > -1)
				return score;

			score = this.board.manhattan() + this.move;
			return score;
		}

		public int compareTo(Solution other) {
			return Integer.compare(this.score(), other.score());
		}

		public Iterator<Board> iterator() {
			Solution current = this;
			Stack<Board> st = new Stack<Board>();
			while (current != null) {
				st.push(current.board);
				current = current.prev;
			}
			return st.iterator();
		}
	}

	private Solution solution = null;

	/** find a solution to the initial board (using the A* algorithm) */
	public Solver(Board initial) {
		if (initial == null)
			throw new IllegalArgumentException();
		this.solution = solve(initial);
	}

	/** is the initial board solvable? (see below) */
	public boolean isSolvable() {
		return solution != null;
	}

	/** min number of moves to solve initial board; -1 if unsolvable */
	public int moves() {
		return (solution != null) ? solution.size() : -1;
	}

	/** sequence of boards in a shortest solution; null if unsolvable */
	public Iterable<Board> solution() {
		return solution;
	}

	private Solution solve(Board initial) {
		MinPQ<Solution> pq = new MinPQ<Solution>();
		MinPQ<Solution> pqTwin = new MinPQ<Solution>();
		pq.insert(new Solution(initial));
		pqTwin.insert(new Solution(initial.twin()));

		while (pq.size() > 0 && pqTwin.size() > 0) {
			// get the most optimal board and put it to the solution
			Solution s = pq.delMin();
			// if that board is terminal - stop
			if (s.board.isGoal()) {
				return s;
			}
			// check twin:
			Solution sTwin = pqTwin.delMin();
			if (sTwin.board.isGoal()) {
				return null;
			}

			for (Board b : s.board.neighbors()) {
				if (s.contains(b)) {
					continue;
				}
				pq.insert(s.add(b));
			}
			for (Board b : sTwin.board.neighbors()) {
				if (sTwin.contains(b)) {
					continue;
				}
				pqTwin.insert(sTwin.add(b));
			}
		}
		throw new IllegalStateException();
	}

	public static void main(String[] args) {

		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] tiles = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				tiles[i][j] = in.readInt();
		Board initial = new Board(tiles);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}

}
