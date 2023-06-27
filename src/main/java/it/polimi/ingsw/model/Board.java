package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.utils.Constants;

import java.io.*;

import static it.polimi.ingsw.utils.Constants.boardSize;

/**
 * A class that represents a Board.
 * This class contains the Item Cards the player can pick from.
 */
public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 6344140278693113L;
    /**
     * The layout of the board.
     */
    private final boolean[][] layout;
    /**
     * The cards placed on the board.
     */
    private final ItemCard[][] tiles;

    /**
     * Creates a new Board with a layout based on the number of players.
     *
     * @param numberOfPlayers the number of players by which the board layout is chosen.
     */
    public Board(int numberOfPlayers) {
        if (numberOfPlayers < Constants.minNumberOfPlayers || numberOfPlayers > Constants.maxNumberOfPlayers) {
            throw new IllegalArgumentException("Number of players must be between 2 and 4");
        }

        tiles = new ItemCard[boardSize][boardSize];

        // open the file corresponding to the chosen number of players (should not fail)
        InputStream inputStream = getClass().getResourceAsStream(String.format("/board/board%d.dat", numberOfPlayers));

        // parse the file as an object, read it and close it
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            layout = (boolean[][]) objectInputStream.readObject();
            objectInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Gets whether the tile at the provided position is valid or not.
     *
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return true if the tile at the provided position is valid, false otherwise.
     */
    public boolean isValid(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column >= boardSize) {
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
        if (row < 0 || row >= boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column >= boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        if (isValid(row, column)) {
            this.tiles[row][column] = itemCard;
        } else throw new IndexOutOfBoundsException();
    }

    /**
     * Gets the card at the given position without removing it.
     *
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, without removing it, or null if the tile is empty.
     */
    public ItemCard peekCard(int row, int column) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (row < 0 || row >= boardSize) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + boardSize
            );
        }

        if (column < 0 || column >= boardSize) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + boardSize
            );
        }

        if (isValid(row, column)) {
            return tiles[row][column];
        } else throw new IndexOutOfBoundsException();
    }

    /**
     * Gets the card at the given position removing it.
     *
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, removing it, or null if the tile is empty.
     */
    public ItemCard popCard(int row, int column) throws IllegalArgumentException, IndexOutOfBoundsException {
        if (row < 0 || row >= boardSize) {
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
     * Gets the layout of the board.
     *
     * @return the matrix that represents the valid tiles.
     */
    public boolean[][] getLayout() {
        return layout;
    }

    /**
     * Gets the Item Cards on the board.
     *
     * @return the Item Cards on the board.
     */
    public ItemCard[][] getTiles() {
        return tiles;
    }

    /**
     * Gets a string representation of this board.
     *
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
