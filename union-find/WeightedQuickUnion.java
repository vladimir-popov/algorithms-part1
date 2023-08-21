import java.util.*;

public class WeightedQuickUnion {

	private int[] node;
	private int[] size;
	private int[] max;

	public WeightedQuickUnion(int N) {
		this.node = new int[N];
		this.size = new int[N];
		this.max = new int[N];
		for (int i = 0; i < N; i++) {
			node[i] = i;
			max[i] = i;
			size[i] = 0;
		}
	}

	public WeightedQuickUnion(int N, List<Integer[]> groups) {
		this(N);
		for (Integer[] group : groups) {
			for (int i = 0; i < group.length - 1; i++) {
				union(group[i], group[i + 1]);
			}
		}
	}

	public boolean connected(int a, int b) {
		return root(a) == root(b);
	}

	public void union(int a, int b) {
		int i = root(a);
		int j = root(b);
		// i - small node; j - big node
		if (size[i] > size[j]) {
			i = i + j;
			j = i - j;
			i = i - j;
		}
		node[i] = j;
		size[j] += size[i];
		if (max[i] > max[j])
			max[j] = max[i];
	}

	public int root(int j) {
		int i = j;
		while (i != node[i])
			i = node[i];
		node[j] = i;
		return i;
	}

	public int find(int i) {
		return max[root(i)];
	}
}
