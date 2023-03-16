package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void TestisValid3() {

        Board board;
        try {
            board = new Board(3);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertEquals(board.isValid(5, 0), 1);
        assertEquals(board.isValid(4, 0), 0);
        assertThrows(IllegalArgumentException.class,
                () -> {
                    board.isValid(9, 0);
                });
    }

    @Test
    void peekCard() {
    }

    @Test
    void popCard() {
    }
}