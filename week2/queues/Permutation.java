import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
	public static void main(String[] args) {
		int k = args.length > 0 ? Integer.parseInt(args[0]) : 0;
		RandomizedQueue<String> q = new RandomizedQueue<>();
		while (!StdIn.isEmpty()) {
			String line = StdIn.readString();
			if (q.size() < k) {
				q.enqueue(line);
			} else if (StdRandom.bernoulli()) {
				q.dequeue();
				q.enqueue(line);
			}
		}
		for (String str : q) {
			StdOut.println(str);
		}
	}
}
