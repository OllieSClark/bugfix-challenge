package challenge;

/** Counting and probability helpers. */
public final class Probability {

    private Probability() {}

    /**
     * Number of ways to choose k items from n ("n choose k").
     * Returns 0 if k is negative or larger than n.
     * Must give exact answers for every result that fits in a long.
     */
    public static long choose(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }

    /**
     * Probability of at least one success in a number of independent attempts,
     * where each attempt succeeds with probability p.
     *
     * @throws IllegalArgumentException if p is outside [0, 1] or trials is negative
     */
    public static double atLeastOne(double p, int trials) {
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("p must be between 0 and 1");
        }
        if (trials < 0) {
            throw new IllegalArgumentException("trials must not be negative");
        }
        return 1 - Math.pow(p, trials);
    }

    /**
     * Probability that at least two people in a group share a birthday,
     * assuming 365 equally likely days and ignoring leap years.
     * A group of 0 or 1 people can never share a birthday.
     */
    public static double birthdayCollision(int people) {
        double allDifferent = 1.0;
        for (int i = 0; i <= people; i++) {
            allDifferent *= (365.0 - i) / 365.0;
        }
        return 1 - allDifferent;
    }
}
