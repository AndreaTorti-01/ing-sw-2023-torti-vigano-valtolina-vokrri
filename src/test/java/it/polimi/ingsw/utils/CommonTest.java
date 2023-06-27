package it.polimi.ingsw.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonTest {

    @Test
    void isTakeable() {

    }

    @Test
    void isAdjacent() {
        assertTrue(Common.isAdjacent(0, 0, 0, 1));
        assertTrue(Common.isAdjacent(0, 0, 1, 0));

        assertFalse(Common.isAdjacent(0, 0, 1, 1));
        assertFalse(Common.isAdjacent(0, 0, 2, 0));
    }

    @Test
    void headLiminate() {

    }

    @Test
    void getHeads() {
    }

    @Test
    void getCommonGoalCardPath() {
    }

    @Test
    void getCommonGoalCardPointPath() {
    }

    @Test
    void getPersonalGoalCardPath() {

    }

    @Test
    void getTilePath() {

    }
}