package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.utils.Common.getLayoutFrom;
import static it.polimi.ingsw.utils.Constants.*;

/**
 * A class that represents a Board.
 * This class contains the Item Cards the player can pick from.
 */
public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 6344140278693113L;
    private final boolean[][] layout;
    private final ItemCard[][] tiles;

    /**
     * Creates a new Board with a layout based on the number of players.
     *
     * @param numberOfPlayers the number of players by which the board layout is chosen.
     */
    public Board(int numberOfPlayers) {
        // checking the numberOfPlayers is valid and setting it in the model
        if (numberOfPlayers < minNumberOfPlayers || numberOfPlayers > maxNumberOfPlayers)
            throw new IllegalArgumentException("provided number of players (" + numberOfPlayers + ") is out of range " + minNumberOfPlayers + "-" + maxNumberOfPlayers);

        this.layout = getLayoutFrom(numberOfPlayers);
        this.tiles = new ItemCard[boardSize][boardSize];
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return true if the tile at the provided position is valid, false otherwise.
     */
    public boolean isValid(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row > boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column > boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        return layout[row][column];
    }

    /**
     * Inserts the provided ItemCard in the tile at the provided position.
     *
     * @param itemCard the Item Card to be inserted or null.
     * @param row      must be between boundaries (provided in the {@link Constants} file).
     * @param column   must be between boundaries (provided in the {@link Constants} file).
     */
    public void setTile(ItemCard itemCard, int row, int column) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (row < 0 || row > boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column > boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        if (isValid(row, column)) {
            this.tiles[row][column] = itemCard;
        } else throw new IndexOutOfBoundsException();
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, without removing it, or null if the tile is empty.
     */
    public ItemCard peekCard(int row, int column) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (row < 0 || row > boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column > boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        if (isValid(row, column)) {
            return tiles[row][column];
        } else throw new IndexOutOfBoundsException();
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, removing it, or null if the tile is empty.
     */
    public ItemCard popCard(int row, int column) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (row < 0 || row > boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column > boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        ItemCard card = peekCard(row, column);
        if (card != null) {
            setTile(null, row, column);
            return card;
        } else throw new IndexOutOfBoundsException();
    }

    /**
     * @return the matrix that represents the valid tiles.
     */
    public boolean[][] getLayout() {
        return layout;
    }

    /**
     * @return the Item Cards on the board.
     */
    public ItemCard[][] getTiles() {
        return tiles;
    }

    /**
     * @return a string representation of this board.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < boardSize; row++) {
            for (int column = 0; column < boardSize; column++) {
                if (isValid(row, column)) {
                    ItemCard card = peekCard(row, column);
                    if (card != null) output.append(card).append(" ");
                    else output.append("* ");
                } else output.append("- ");

            }
            output.append("\n");
        }

        return output.toString();
    }
}
