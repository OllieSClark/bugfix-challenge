package challenge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Splitting a dataset into training and test sets. */
public final class DataSplit {

    private DataSplit() {}

    /** Row indices chosen for training and for testing. */
    public record Split(int[] train, int[] test) {}

    /**
     * Randomly splits the row indices 0..n-1 into a training set and a test set.
     * <ul>
     *   <li>The test set has round(n * testFraction) rows.</li>
     *   <li>Every index ends up in exactly one of the two sets (no row in both).</li>
     *   <li>The same seed always gives the same split.</li>
     * </ul>
     *
     * @throws IllegalArgumentException if n is negative or testFraction is outside [0, 1]
     */
    public static Split trainTestSplit(int n, double testFraction, long seed) {
        if (n < 0) {
            throw new IllegalArgumentException("n must not be negative");
        }
        if (testFraction < 0 || testFraction > 1) {
            throw new IllegalArgumentException("testFraction must be between 0 and 1");
        }
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, new Random(seed));

        int testSize = (int) Math.round(n * testFraction);
        int[] test = toArray(indices.subList(0, testSize));
        int[] train = toArray(indices.subList(testSize - 1, n));
        return new Split(train, test);
    }

    private static int[] toArray(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).toArray();
    }
}
