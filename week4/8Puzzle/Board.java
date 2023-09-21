
public class Board {

	private final int N;
	private final int[][] tiles;

	private int zeroIdx;
	// lazy val
	private int hamming = -1;
	// lazy val
	private int manhattan = -1;

	/**
	 * Creates a board from an n-by-n array of tiles, where tiles[row][col] = tile
	 * at (row, col)
	 */
	public Board(int[][] tiles) {
		assert (2 <= tiles.length && tiles.length < 128);
		this.N = tiles.length;
		this.tiles = new int[N][N];
		for (int i = 0; i < N; i++) {
			assert (tiles.length == tiles[i].length);
			for (int j = 0; j < N; j++) {
				this.tiles[i][j] = tiles[i][j];
				if (tiles[i][j] == 0)
					zeroIdx = i * N + j;
			}
		}
	}

	/** string representation of this board */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(N);
		for (int i = 0; i < N; i++) {
			sb.append("\n");
			for (int j = 0; j < N; j++) {
				sb.append(tiles[i][j]);
				if (j < N - 1)
					sb.append(" ");
			}
		}
		return sb.toString();
	}

	/** board dimension n */
	public int dimension() {
		return N;
	}

	/** number of tiles out of place */
	public int hamming() {
		if (hamming != -1)
			return hamming;

		hamming = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] != i * N + j + 1)
					hamming++;
			}
		// 0 is not counted
		hamming--;
		return hamming;
	}

	/** sum of Manhattan distances between tiles and goal */
	public int manhattan() {
		if (manhattan != -1)
			return manhattan;

		manhattan = 0;
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++) {
				if (tiles[i][j] == 0)
					continue;
				manhattan += Math.abs(i - (tiles[i][j] - 1) / N);
				manhattan += Math.abs(j - (tiles[i][j] - 1) % N);
			}
		return manhattan;
	}

	/** is this board the goal board? */
	public boolean isGoal() {
		return zeroIdx == (N * N - 1) && hamming() == 0;
	}

	/** does this board equal y? */
	public boolean equals(Object y) {
		if (this == y)
			return true;

		if (y == null || !(y instanceof Board))
			return false;

		Board other = (Board) y;
		if (this.dimension() != other.dimension())
			return false;

		if (this.zeroIdx != other.zeroIdx)
			return false;

		if (this.manhattan != -1 && other.manhattan != -1 && this.manhattan != other.manhattan)
			return false;

		if (this.hamming != -1 && other.hamming != -1 && this.hamming != other.hamming)
			return false;

		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				if (this.tiles[i][j] != other.tiles[i][j])
					return false;

		return true;
	}

	/** all neighboring boards */
	public Iterable<Board> neighbors() {
		Board[] neighbors = { moveUpper(), moveRight(), moveLower(), moveLeft() };
		return () -> java.util.Arrays.stream(neighbors).filter(x -> x != null).iterator();
	}

	/** a board that is obtained by exchanging any pair of tiles */
	public Board twin() {
		for (int i = N - 1; i >= 0; i--)
			for (int j = N - 1; j > 0; j--) {
				if (tiles[i][j] == 0 || tiles[i][j - 1] == 0)
					continue;

				Board b = new Board(tiles);
				int tmp = b.tiles[i][j];
				b.tiles[i][j] = b.tiles[i][j - 1];
				b.tiles[i][j - 1] = tmp;
				return b;
			}
		throw new IllegalStateException();
	}

	// unit testing (not graded)
	public static void main(String[] args) {
	}

	private Board moveUpper() {
		int i0 = zeroIdx / N;
		if (i0 == 0)
			return null;
		int j0 = zeroIdx % N;
		return swapToZero(i0 - 1, j0);
	}

	private Board moveLower() {
		int i0 = zeroIdx / N;
		if (i0 == N - 1)
			return null;
		int j0 = zeroIdx % N;
		return swapToZero(i0 + 1, j0);
	}

	private Board moveLeft() {
		int j0 = zeroIdx % N;
		if (j0 == 0)
			return null;
		int i0 = zeroIdx / N;
		return swapToZero(i0, j0 - 1);
	}

	private Board moveRight() {
		int j0 = zeroIdx % N;
		if (j0 == N - 1)
			return null;
		int i0 = zeroIdx / N;
		return swapToZero(i0, j0 + 1);
	}

	private Board swapToZero(int i, int j) {
		int i0 = zeroIdx / N;
		int j0 = zeroIdx % N;
		Board copy = new Board(tiles);
		copy.tiles[i0][j0] = this.tiles[i][j];
		copy.tiles[i][j] = 0;
		copy.zeroIdx = i * N + j;
		return copy;
	}
}
