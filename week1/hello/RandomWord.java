import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomWord {

    public static void main(String[] args) {
        String winner = null;
        String word = null;
        int i = 0;
        while (!StdIn.isEmpty()) {
            word = StdIn.readString();
            i++;
            if (StdRandom.bernoulli(1.0 / i)) {
                winner = word;
            }
        }
        StdOut.println(winner);
    }
}
