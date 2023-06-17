package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.utils.Common.getLayoutFrom;
import static it.polimi.ingsw.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void testBoardWithValidNumberOfPlayers() {
        for (int i = minNumberOfPlayers; i <= maxNumberOfPlayers; i++) {
            final int numberOfPlayers = i;
            assertDoesNotThrow(() -> new Board(numberOfPlayers));
        }
    }

    @Test
    void testBoardWithInvalidNumberOfPlayers() {
        assertThrows(IllegalArgumentException.class, () -> new Board(1));
        assertThrows(IllegalArgumentException.class, () -> new Board(5));
    }

    @Test
    void testValidity() {
        for (int numberOfPlayers = minNumberOfPlayers; numberOfPlayers <= maxNumberOfPlayers; numberOfPlayers++) {
            Board board = new Board(numberOfPlayers);
            boolean[][] layout = getLayoutFrom(numberOfPlayers);

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    assertEquals(board.isValid(row, col), layout[row][col]);
                }
            }
        }
    }

    @Test
    void testSetTile() {
        // arbitrary number of players
        Board board = new Board(4);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                final int row = i;
                final int col = j;
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType(), 0);

                if (board.isValid(i, j)) {
                    assertDoesNotThrow(() -> board.setTile(randomItemCard, row, col));
                } else {
                    assertThrows(IndexOutOfBoundsException.class, () -> board.setTile(randomItemCard, row, col));
                }
            }
        }

        ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType(), 0);
        assertThrows(IllegalArgumentException.class, () -> board.setTile(randomItemCard, -1, -1));
    }

    @Test
    void testPeekCard() {
        // arbitrary number of players
        Board board = new Board(4);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType(), 0);
                final int row = i;
                final int col = j;
                if (board.isValid(row, col)) {
                    board.setTile(randomItemCard, row, col);
                    assertEquals(randomItemCard, board.peekCard(row, col));
                } else {
                    assertThrows(IndexOutOfBoundsException.class, () -> board.peekCard(row, col));
                }
            }
        }

        assertThrows(IllegalArgumentException.class, () -> board.peekCard(-1, -1));
    }

    @Test
    void testPopCard() {
        // arbitrary number of players
        Board board = new Board(4);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType(), 0);
                final int row = i;
                final int col = j;

                if (board.isValid(row, col)) {
                    board.setTile(randomItemCard, row, col);
                    assertEquals(randomItemCard, board.popCard(row, col));
                } else {
                    assertThrows(IndexOutOfBoundsException.class, () -> board.peekCard(row, col));
                }
            }
        }

        assertThrows(IllegalArgumentException.class, () -> board.popCard(-1, -1));
    }
}