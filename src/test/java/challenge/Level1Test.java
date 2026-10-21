package challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Level 1: the basics. Start here. */
class Level1Test {

    private static final double EPS = 1e-9;

    @Test
    @DisplayName("sum of 1, 2, 3, 4 is 10")
    void sumOfSmallArray() {
        assertEquals(10.0, Stats.sum(new double[] {1, 2, 3, 4}), EPS);
    }

    @Test
    @DisplayName("sum of an empty array is 0")
    void sumOfEmptyArrayIsZero() {
        assertEquals(0.0, Stats.sum(new double[] {}), EPS);
    }

    @Test
    @DisplayName("mean of 2, 4, 6 is 4")
    void meanOfSmallArray() {
        assertEquals(4.0, Stats.mean(new double[] {2, 4, 6}), EPS);
    }

    @Test
    @DisplayName("mean of an empty array throws IllegalArgumentException")
    void meanOfEmptyArrayThrows() {
        assertThrows(IllegalArgumentException.class, () -> Stats.mean(new double[] {}));
    }

    @Test
    @DisplayName("max of 3, 9, 2 is 9")
    void maxOfPositives() {
        assertEquals(9.0, Stats.max(new double[] {3, 9, 2}), EPS);
    }

    @Test
    @DisplayName("max of -5, -2, -9 is -2")
    void maxOfNegatives() {
        assertEquals(-2.0, Stats.max(new double[] {-5, -2, -9}), EPS);
    }

    @Test
    @DisplayName("min of 3, -1, 8 is -1")
    void minOfMixed() {
        assertEquals(-1.0, Stats.min(new double[] {3, -1, 8}), EPS);
    }
}
