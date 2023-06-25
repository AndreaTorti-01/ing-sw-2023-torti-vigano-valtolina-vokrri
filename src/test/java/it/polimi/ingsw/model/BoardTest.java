package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private final boolean[][] twoPlayersBoardLayout = new boolean[][]{
            {false, false, false, false, false, false, false, false, false},
            {false, false, false, true, true, false, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, true, false},
            {false, true, true, true, true, true, true, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, false, true, true, false, false, false},
            {false, false, false, false, false, false, false, false, false},
    };
    private final boolean[][] threePlayersBoardLayout = new boolean[][]{
            {false, false, false, true, false, false, false, false, false},
            {false, false, false, true, true, false, false, false, false},
            {false, false, true, true, true, true, true, false, false},
            {false, false, true, true, true, true, true, true, true},
            {false, true, true, true, true, true, true, true, false},
            {true, true, true, true, true, true, true, false, false},
            {false, false, true, true, true, true, true, false, false},
            {false, false, false, false, true, true, false, false, false},
            {false, false, false, false, false, true, false, false, false},

    };
    private final boolean[][] fourPlayersBoardLayout = new boolean[][]{
            {false, false, false, true, true, false, false, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, true, true, true, true, true, false, false},
            {false, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, false},
            {false, false, true, true, true, true, true, false, false},
            {false, false, false, true, true, true, false, false, false},
            {false, false, false, false, true, true, false, false, false},
    };

    /**
     * @param numberOfPlayers the number of players.
     * @return a boolean matrix that represents the board's active tiles.
     */
    private boolean[][] getLayoutFrom(int numberOfPlayers) {
        return switch (numberOfPlayers) {
            case 2 -> twoPlayersBoardLayout;
            case 3 -> threePlayersBoardLayout;
            case 4 -> fourPlayersBoardLayout;
            default -> new boolean[boardSize][boardSize];
        };
    }

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
    void testIsValid() {
        boolean[][] layout;
        Board board;
        for (int numberOfPlayers = minNumberOfPlayers; numberOfPlayers <= maxNumberOfPlayers; numberOfPlayers++) {
            board = new Board(numberOfPlayers);
            layout = getLayoutFrom(numberOfPlayers);

            for (int row = 0; row < boardSize; row++) {
                for (int col = 0; col < boardSize; col++) {
                    assertEquals(board.isValid(row, col), layout[row][col]);
                }
            }
            Board finalBoard = board;
            assertThrows(IllegalArgumentException.class, () -> finalBoard.isValid(-1, -1));
            assertThrows(IllegalArgumentException.class, () -> finalBoard.isValid(0, -1));
            assertThrows(IllegalArgumentException.class, () -> finalBoard.isValid(boardSize, boardSize));
            assertThrows(IllegalArgumentException.class, () -> finalBoard.isValid(0, boardSize));

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
        assertThrows(IllegalArgumentException.class, () -> board.setTile(randomItemCard, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> board.setTile(randomItemCard, boardSize, boardSize));
        assertThrows(IllegalArgumentException.class, () -> board.setTile(randomItemCard, 0, boardSize));
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
        assertThrows(IllegalArgumentException.class, () -> board.peekCard(0, -1));
        assertThrows(IllegalArgumentException.class, () -> board.peekCard(boardSize, boardSize));
        assertThrows(IllegalArgumentException.class, () -> board.peekCard(0, boardSize));
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
        assertThrows(IllegalArgumentException.class, () -> board.popCard(0, -1));
        assertThrows(IllegalArgumentException.class, () -> board.popCard(boardSize, boardSize));
        assertThrows(IllegalArgumentException.class, () -> board.popCard(0, boardSize));
    }
}