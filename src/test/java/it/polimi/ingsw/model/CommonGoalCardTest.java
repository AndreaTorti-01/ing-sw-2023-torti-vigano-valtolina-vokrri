package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class CommonGoalCardTest {

    @Test
    void testFourPlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4);

        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 6);
        assertEquals(commonGoalCard.popPoints(), 6);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);

        assertEquals(commonGoalCard.peekPoints(), 2);
        assertEquals(commonGoalCard.popPoints(), 2);

        assertTrue(
                CommonGoalCardType.getValues().contains(commonGoalCard.getType())
        );
    }

    @Test
    void testThreePlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(3);

        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 6);
        assertEquals(commonGoalCard.popPoints(), 6);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);

        assertTrue(
                CommonGoalCardType.getValues().contains(commonGoalCard.getType())
        );
    }

    @Test
    void testTwoPlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(2);


        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);

        assertTrue(
                CommonGoalCardType.getValues().contains(commonGoalCard.getType())
        );
    }

    @Test
    void testPopMorePointsThanAvailable() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4);

        // pops all available points
        for (int i = 0; i < 4; i++)
            commonGoalCard.popPoints();

        assertEquals(commonGoalCard.popPoints(), 0);
    }

    @Test
    void testPeekMorePointsThanAvailable() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4);

        // pops all available points
        for (int i = 0; i < 4; i++)
            commonGoalCard.popPoints();

        assertEquals(commonGoalCard.peekPoints(), 0);
    }

    @Test
    void testMoreThanFourPlayers() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommonGoalCard(5)
        );
    }

    @Test
    void testLessThanTwoPlayers() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommonGoalCard(1)
        );
    }
}