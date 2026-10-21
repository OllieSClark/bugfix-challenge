package challenge;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Level 2: statistics and scaling. Some of these only break once Level 1 is fixed. */
class Level2Test {

    private static final double EPS = 1e-9;
    private static final double[] SAMPLE = {2, 4, 4, 4, 5, 5, 7, 9};

    @Test
    @DisplayName("median of 5, 1, 3 is 3 (unsorted, odd length)")
    void medianOddLengthUnsorted() {
        assertEquals(3.0, Stats.median(new double[] {5, 1, 3}), EPS);
    }

    @Test
    @DisplayName("median of 1, 2, 3, 4 is 2.5 (sorted, even length)")
    void medianEvenLengthSorted() {
        assertEquals(2.5, Stats.median(new double[] {1, 2, 3, 4}), EPS);
    }

    @Test
    @DisplayName("median of 4, 1, 3, 2 is 2.5 (unsorted, even length)")
    void medianEvenLengthUnsorted() {
        assertEquals(2.5, Stats.median(new double[] {4, 1, 3, 2}), EPS);
    }

    @Test
    @DisplayName("median leaves the input array unchanged")
    void medianDoesNotModifyInput() {
        double[] xs = {3, 1, 2};
        Stats.median(xs);
        assertArrayEquals(new double[] {3, 1, 2}, xs, EPS);
    }

    @Test
    @DisplayName("sample variance of 2, 4, 4, 4, 5, 5, 7, 9 is 32/7")
    void varianceOfKnownData() {
        assertEquals(32.0 / 7, Stats.variance(SAMPLE), EPS);
    }

    @Test
    @DisplayName("sample standard deviation of 2, 4, 4, 4, 5, 5, 7, 9 is sqrt(32/7)")
    void stdDevOfKnownData() {
        assertEquals(Math.sqrt(32.0 / 7), Stats.stdDev(SAMPLE), EPS);
    }

    @Test
    @DisplayName("variance of a single value throws IllegalArgumentException")
    void varianceNeedsTwoValues() {
        assertThrows(IllegalArgumentException.class, () -> Stats.variance(new double[] {1}));
    }

    @Test
    @DisplayName("minMax of 10, 20, 30 is 0, 0.5, 1")
    void minMaxScalesToUnitInterval() {
        assertArrayEquals(new double[] {0, 0.5, 1}, Scaler.minMax(new double[] {10, 20, 30}), EPS);
    }

    @Test
    @DisplayName("minMax of -4, -2, -1 is 0, 2/3, 1")
    void minMaxHandlesNegatives() {
        assertArrayEquals(
                new double[] {0, 2.0 / 3, 1}, Scaler.minMax(new double[] {-4, -2, -1}), EPS);
    }

    @Test
    @DisplayName("minMax of a constant array is all zeros (not NaN)")
    void minMaxConstantArrayGivesZeros() {
        assertArrayEquals(new double[] {0, 0, 0}, Scaler.minMax(new double[] {5, 5, 5}), EPS);
    }

    @Test
    @DisplayName("z-scores of 2, 4, 4, 4, 5, 5, 7, 9 have mean 0 and standard deviation 1")
    void zScoresAreStandardised() {
        double[] z = Scaler.zScores(SAMPLE);
        assertEquals((2 - 5) / Math.sqrt(32.0 / 7), z[0], EPS);
        assertEquals(0.0, Stats.mean(z), EPS);
        assertEquals(1.0, Stats.stdDev(z), EPS);
    }
}
