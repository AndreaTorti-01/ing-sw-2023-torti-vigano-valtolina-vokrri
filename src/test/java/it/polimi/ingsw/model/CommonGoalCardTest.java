package it.polimi.ingsw.model;

import it.polimi.ingsw.model.commonGoalCardStrats.CommonGoalCardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CommonGoalCardTest {

    private CommonGoalCardStrat randomStrat;

    @BeforeEach
    void createRandomStrategy() {
        CommonGoalCardType randomType = CommonGoalCardType.values()[new Random().nextInt(0, 12)];
        randomStrat = CommonGoalCardType.getStrategyFromType(randomType);
    }


    @Test
    void testFourPlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4, randomStrat);

        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 6);
        assertEquals(commonGoalCard.popPoints(), 6);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);

        assertEquals(commonGoalCard.peekPoints(), 2);
        assertEquals(commonGoalCard.popPoints(), 2);
    }

    @Test
    void testThreePlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(3, randomStrat);

        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 6);
        assertEquals(commonGoalCard.popPoints(), 6);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);
    }


    @Test
    void testTwoPlayersFunctionalities() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(2, randomStrat);


        assertEquals(commonGoalCard.peekPoints(), 8);
        assertEquals(commonGoalCard.popPoints(), 8);

        assertEquals(commonGoalCard.peekPoints(), 4);
        assertEquals(commonGoalCard.popPoints(), 4);
    }


    @Test
    void testPopMorePointsThanAvailable() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4, randomStrat);

        // pops all available points
        for (int i = 0; i < 4; i++)
            commonGoalCard.popPoints();

        assertEquals(commonGoalCard.popPoints(), 0);
    }

    @Test
    void testPeekMorePointsThanAvailable() {
        CommonGoalCard commonGoalCard = new CommonGoalCard(4, randomStrat);

        // pops all available points
        for (int i = 0; i < 4; i++)
            commonGoalCard.popPoints();

        assertEquals(commonGoalCard.peekPoints(), 0);
    }

    @Test
    void testIllegalNumberOfPlayers() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CommonGoalCard(5, randomStrat)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new CommonGoalCard(1, randomStrat)
        );
    }
}