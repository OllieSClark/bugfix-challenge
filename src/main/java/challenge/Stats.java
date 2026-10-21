package challenge;

/**
 * Basic descriptive statistics.
 * Every method treats the array as a complete sample and must not modify it.
 */
public final class Stats {

    private Stats() {}

    /** Sum of all values. The sum of an empty array is 0. */
    public static double sum(double[] xs) {
        double total = 0;
        for (int i = 0; i < xs.length - 1; i++) {
            total += xs[i];
        }
        return total;
    }

    /**
     * Arithmetic mean.
     *
     * @throws IllegalArgumentException if xs is empty
     */
    public static double mean(double[] xs) {
        requireNonEmpty(xs);
        return sum(xs) / xs.length;
    }

    /**
     * Largest value.
     *
     * @throws IllegalArgumentException if xs is empty
     */
    public static double max(double[] xs) {
        requireNonEmpty(xs);
        double best = 0;
        for (double x : xs) {
            if (x > best) {
                best = x;
            }
        }
        return best;
    }

    /**
     * Smallest value.
     *
     * @throws IllegalArgumentException if xs is empty
     */
    public static double min(double[] xs) {
        requireNonEmpty(xs);
        double best = Double.POSITIVE_INFINITY;
        for (double x : xs) {
            if (x < best) {
                best = x;
            }
        }
        return best;
    }

    /**
     * Median: the middle value of the sorted data, or the mean of the two
     * middle values when there is an even number of values.
     * The input array must be left exactly as it was passed in.
     *
     * @throws IllegalArgumentException if xs is empty
     */
    public static double median(double[] xs) {
        requireNonEmpty(xs);
        double[] copy = xs.clone();
        int n = copy.length;
        if (n % 2 == 1) {
            return copy[n / 2];
        }
        return (copy[n / 2 - 1] + copy[n / 2]) / 2;
    }

    /**
     * Sample variance (divides by n - 1).
     *
     * @throws IllegalArgumentException if xs has fewer than two values
     */
    public static double variance(double[] xs) {
        if (xs.length < 2) {
            throw new IllegalArgumentException("need at least two values");
        }
        double m = mean(xs);
        double squares = 0;
        for (double x : xs) {
            squares += (x - m) * (x - m);
        }
        return squares / xs.length;
    }

    /** Sample standard deviation: the square root of the sample variance. */
    public static double stdDev(double[] xs) {
        return Math.sqrt(variance(xs));
    }

    private static void requireNonEmpty(double[] xs) {
        if (xs.length == 0) {
            throw new IllegalArgumentException("array is empty");
        }
    }
}
