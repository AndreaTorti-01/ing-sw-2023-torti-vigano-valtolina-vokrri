package it.polimi.ingsw.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class Board extends GameObject {
    private final boolean[][] valid;
    private final ItemCard[][] tile;

    public Board(int playerCount) throws FileNotFoundException {
        // deferred valid assignment because of multiple try/catches
        boolean[][] valid1;
        try {
            // open the file corresponding to the chosen number of players
            InputStream inputStream = getClass().getResourceAsStream(String.format("/board/board%d.dat", playerCount));
            // parse the file as an object
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // read the object: might fail
            valid1 = (boolean[][]) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            valid1 = null;
            e.printStackTrace();
        }
        valid = valid1;
        tile = new ItemCard[9][9];
    }

    public boolean isValid(int row, int col) {
        return valid[row][col];
    }

    public void setTile(ItemCard tile, int row, int col) throws ArrayIndexOutOfBoundsException {
        // go out of bounds even if the tile is not valid
        if (isValid(row, col)) {
            this.tile[row][col] = tile;
        } else throw new ArrayIndexOutOfBoundsException();
    }

    public ItemCard peekCard(int row, int col) throws ArrayIndexOutOfBoundsException {
        // go out of bounds even if the tile is not valid
        if (isValid(row, col)) {
            return tile[row][col];
        } else throw new ArrayIndexOutOfBoundsException();
    }

    public ItemCard popCard(int row, int col) throws NullPointerException, ArrayIndexOutOfBoundsException {
        ItemCard card;
        try {
            // try to get the card and set it to null
            card = peekCard(row, col);
            if (card != null) {
                setTile(null, row, col);
                return card;
            } else throw new NullPointerException();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }
}
