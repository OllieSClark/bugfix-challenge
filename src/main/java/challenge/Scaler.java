package challenge;

/** Feature scaling, the first step of most machine-learning pipelines. */
public final class Scaler {

    private Scaler() {}

    /**
     * Rescales values linearly so the smallest becomes 0 and the largest becomes 1:
     * (x - min) / (max - min). A constant array (no spread) becomes all zeros.
     */
    public static double[] minMax(double[] xs) {
        double min = Stats.min(xs);
        double max = Stats.max(xs);
        double[] out = new double[xs.length];
        for (int i = 0; i < xs.length; i++) {
            out[i] = (xs[i] - min) / max;
        }
        return out;
    }

    /**
     * Standardises values to z-scores: (x - mean) / sample standard deviation.
     * The result has mean 0 and sample standard deviation 1.
     * A constant array (no spread) becomes all zeros.
     *
     * @throws IllegalArgumentException if xs has fewer than two values
     */
    public static double[] zScores(double[] xs) {
        double m = Stats.mean(xs);
        double sd = Stats.stdDev(xs);
        double[] out = new double[xs.length];
        if (sd == 0) {
            return out;
        }
        for (int i = 0; i < xs.length; i++) {
            out[i] = (xs[i] - m) / sd;
        }
        return out;
    }
}
