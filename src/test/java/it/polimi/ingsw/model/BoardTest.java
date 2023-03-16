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

        assertTrue(board.isValid(5, 0));
        assertFalse(board.isValid(4, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> {
                    board.isValid(9, 0);
                });
    }

    @Test
    void TestisValid2() {

        Board board;
        try {
            board = new Board(2);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertTrue(board.isValid(1, 3));
        assertFalse(board.isValid(4, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> {
                    board.isValid(9, 0);
                });
    }

    @Test
    void TestisValid4() {
        Board board;
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertTrue(board.isValid(4, 0));
        assertFalse(board.isValid(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> {
                    board.isValid(9, 0);
                });
    }


    @Test
    void peekCard() {
        // setup
        Board board;
        ItemCard itemCard = new ItemCard(ItemType.BOOKS);
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.setTile(itemCard, 4, 4);

        // inizio test
        assertEquals(itemCard, board.peekCard(4, 4));
        assertEquals(null, board.peekCard(3, 4));

    }

    @Test
    void popCard() {
    }
}