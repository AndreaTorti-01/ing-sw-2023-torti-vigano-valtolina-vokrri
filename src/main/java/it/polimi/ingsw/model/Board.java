package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Board extends GameObject {
    private final boolean[][] valid;
    private final ItemCard[][] tile;

    /**
     * The file is read through the object loader ObjectInputStream
     *
     * @param numberOfPlayers is the number of players: needed to choose the board layout
     * @throws FileNotFoundException if the number of players is not between 2 and 4
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
        tile = new ItemCard[9][9];
    }

    /**
     * @param row
     * @param column
     * @return true or false depending on the validity of the selected tile in the current game
     */
    public boolean isValid(int row, int column) {
        return valid[row][column];
    }

    /**
     * @param tile   may be ItemCard or null
     * @param row    must be valid
     * @param column must be valid
     * @throws ArrayIndexOutOfBoundsException if (row,column) is not a valid position
     */
    public void setTile(ItemCard tile, int row, int column) throws ArrayIndexOutOfBoundsException {
        // go out of bounds even if the tile is not valid
        if (isValid(row, column)) {
            this.tile[row][column] = tile;
        } else throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @param row    must be valid
     * @param column must be valid
     * @return the card on the selected tile, without removing it, or null if the tile is empty
     * @throws ArrayIndexOutOfBoundsException if (row,column) is not a valid position
     */
    public ItemCard peekCard(int row, int column) throws ArrayIndexOutOfBoundsException {
        // go out of bounds even if the tile is not valid
        if (isValid(row, column)) {
            return tile[row][column];
        } else throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * @param row    must be valid
     * @param column must be valid
     * @return the card on the selected tile, removing it, or null if the tile is empty
     * @throws NullPointerException           if the tile is already empty
     * @throws ArrayIndexOutOfBoundsException if (row,column) is not a valid position
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
}
