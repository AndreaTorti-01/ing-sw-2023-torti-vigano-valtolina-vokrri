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
    private final boolean[][] valid;
    private final ItemCard[][] tile;

    /**
     * Creates a new Board with a layout based on the number of players.
     *
     * @param numberOfPlayers the number of players by which the board layout is chosen.
     * @throws FileNotFoundException if the provided number of players is not valid (provided in the {@link Constants} file)
     */
    public Board(int numberOfPlayers) throws FileNotFoundException {
        // deferred valid assignment because of multiple try/catches
        boolean[][] valid1;
        try {
            // open the file corresponding to the chosen number of players
            InputStream inputStream = getClass().getResourceAsStream(String.format("/board/board%d.dat", numberOfPlayers));
            // parse the file as an object
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // read the object: might fail
            valid1 = (boolean[][]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            valid1 = null;
            e.printStackTrace();
        }
        if (valid1 == null) throw new FileNotFoundException();
        // assign the valid matrix
        valid = valid1;
        tile = new ItemCard[boardSize][boardSize];
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return true if the tile at the provided position is valid, false otherwise.
     */
    public boolean isValid(int row, int column) {
        return valid[row][column];
    }

    /**
     * Inserts the provided ItemCard in the tile at the provided position.
     *
     * @param tile   may be ItemCard or null.
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @throws ArrayIndexOutOfBoundsException if the provided position is not valid.
     */
    public void setTile(ItemCard tile, int row, int column) throws ArrayIndexOutOfBoundsException {
        if (isValid(row, column)) {
            this.tile[row][column] = tile;
        } else throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, without removing it, or null if the tile is empty.
     * @throws ArrayIndexOutOfBoundsException if the provided position is not valid.
     */
    public ItemCard peekCard(int row, int column) throws ArrayIndexOutOfBoundsException {
        // go out of bounds even if the tile is not valid
        if (isValid(row, column)) {
            return tile[row][column];
        } else throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the card on the selected tile, removing it, or null if the tile is empty.
     * @throws NullPointerException           if the tile is already empty.
     * @throws ArrayIndexOutOfBoundsException if the provided position is not valid.
     */
    public ItemCard popCard(int row, int column) throws NullPointerException, ArrayIndexOutOfBoundsException {
        ItemCard card;
        try {
            // try to get the card and set it to null
            card = peekCard(row, column);
            if (card != null) {
                setTile(null, row, column);
                return card;
            } else throw new NullPointerException();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the matrix that represents the valid tiles.
     */
    public boolean[][] getValidMatrix() {
        return valid;
    }

    public ItemCard[][] getTileMatrix() {
        return tile;
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
