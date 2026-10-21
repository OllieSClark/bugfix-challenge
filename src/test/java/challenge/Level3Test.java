package challenge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Level 3: probability and data science pitfalls. A passing test is not proof of correct code. */
class Level3Test {

    private static final double EPS = 1e-9;

    // ---- counting ----

    @Test
    @DisplayName("5 choose 2 is 10")
    void chooseSmall() {
        assertEquals(10L, Probability.choose(5, 2));
    }

    @Test
    @DisplayName("choose edge cases: 7 choose 0 = 1, 7 choose 7 = 1, 3 choose 5 = 0")
    void chooseEdgeCases() {
        assertEquals(1L, Probability.choose(7, 0));
        assertEquals(1L, Probability.choose(7, 7));
        assertEquals(0L, Probability.choose(3, 5));
    }

    @Test
    @DisplayName("40 choose 20 is 137846528820")
    void chooseLarge() {
        assertEquals(137_846_528_820L, Probability.choose(40, 20));
    }

    // ---- probability ----

    @Test
    @DisplayName("at least one head in 3 fair coin flips: 0.875")
    void atLeastOneFairCoin() {
        assertEquals(0.875, Probability.atLeastOne(0.5, 3), EPS);
    }

    @Test
    @DisplayName("at least one success in 2 attempts at p = 0.1: 0.19")
    void atLeastOneRareEvent() {
        assertEquals(0.19, Probability.atLeastOne(0.1, 2), EPS);
    }

    @Test
    @DisplayName("atLeastOne rejects a probability above 1")
    void atLeastOneRejectsBadProbability() {
        assertThrows(IllegalArgumentException.class, () -> Probability.atLeastOne(1.5, 3));
    }

    @Test
    @DisplayName("birthday problem: 23 people give about a 50.73% chance of a shared birthday")
    void birthdayTwentyThree() {
        assertEquals(0.5073, Probability.birthdayCollision(23), 1e-3);
    }

    @Test
    @DisplayName("birthday problem: more than 365 people means a shared birthday is certain")
    void birthdayPigeonhole() {
        assertEquals(1.0, Probability.birthdayCollision(366), EPS);
    }

    // ---- train / test split ----

    @Test
    @DisplayName("100 rows with testFraction 0.2 gives 80 train rows and 20 test rows")
    void splitSizes() {
        DataSplit.Split s = DataSplit.trainTestSplit(100, 0.2, 42L);
        assertEquals(80, s.train().length);
        assertEquals(20, s.test().length);
    }

    @Test
    @DisplayName("no row appears in both the train and the test set (no data leakage!)")
    void splitHasNoOverlap() {
        DataSplit.Split s = DataSplit.trainTestSplit(100, 0.2, 42L);
        Set<Integer> train = toSet(s.train());
        for (int row : s.test()) {
            assertTrue(!train.contains(row), "row " + row + " is in both train and test");
        }
    }

    @Test
    @DisplayName("every row 0..49 appears in the train or the test set")
    void splitCoversEveryRow() {
        DataSplit.Split s = DataSplit.trainTestSplit(50, 0.3, 7L);
        Set<Integer> all = toSet(s.train());
        all.addAll(toSet(s.test()));
        assertEquals(50, all.size());
    }

    @Test
    @DisplayName("the same seed gives the same split")
    void splitIsReproducible() {
        DataSplit.Split a = DataSplit.trainTestSplit(60, 0.25, 123L);
        DataSplit.Split b = DataSplit.trainTestSplit(60, 0.25, 123L);
        assertArrayEquals(a.test(), b.test());
        assertTrue(Arrays.equals(a.train(), b.train()));
    }

    private static Set<Integer> toSet(int[] xs) {
        Set<Integer> out = new HashSet<>();
        for (int x : xs) {
            out.add(x);
        }
        return out;
    }
}
