package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void boardTest() {
        assertThrows(Exception.class, () -> {
            Board board = new Board(5);
        });
    }

    @Test
    void isValidTest() {
        // setup
        Board board2;
        try {
            board2 = new Board(2);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertTrue(board2.isValid(1, 3));
        assertFalse(board2.isValid(4, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board2.isValid(9, 0);
        });

        Board board3;
        try {
            board3 = new Board(3);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        assertTrue(board3.isValid(5, 0));
        assertFalse(board3.isValid(4, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board3.isValid(9, 0);
        });

        Board board4;
        try {
            board4 = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // test
        assertTrue(board4.isValid(4, 0));
        assertFalse(board4.isValid(3, 0));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board4.isValid(9, 0);
        });
    }

    @Test
    void setTileTest() {
        Board board;
        ItemCard itemCard = new ItemCard(ItemType.CATS, 0);
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.setTile(itemCard, 5, 5);

        assertEquals(itemCard, board.peekCard(5, 5));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.setTile(itemCard, 0, 0);
        });
    }

    @Test
    void peekCardTest() {
        // setup
        Board board;
        ItemCard itemCard = new ItemCard(ItemType.BOOKS, 0);
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.setTile(itemCard, 4, 4);

        // test
        assertEquals(itemCard, board.peekCard(4, 4));
        assertNull(board.peekCard(3, 4));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            board.peekCard(0, 0);
        });

    }

    @Test
    void popCardTest() {
        // setup
        Board board;
        ItemCard itemCard = new ItemCard(ItemType.CATS, 0);
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.setTile(itemCard, 5, 5);

        // test
        ItemCard itemCardTest = board.popCard(5, 5);
        assertEquals(itemCard, itemCardTest);
        assertNull(board.peekCard(5, 5));

        assertThrows(NullPointerException.class, () -> {
            board.popCard(4, 3);
        });
    }

    @Test
    void toStringTest() {
        Board board;
        try {
            board = new Board(4);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        board.setTile(new ItemCard(ItemType.CATS, 0), 5, 5);
        board.setTile(new ItemCard(ItemType.BOOKS, 0), 4, 5);
        System.out.println(board);
    }
}