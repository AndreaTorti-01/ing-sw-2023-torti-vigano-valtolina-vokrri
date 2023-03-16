package it.polimi.ingsw.model;

import java.io.*;

public class Board extends GameObject {
    private boolean[][] valid;
    private ItemCard[][] tile;

    public Board(int playerCount) throws FileNotFoundException {
        InputStream inputStream = getClass().getResourceAsStream(String.format("/Board/board%d.dat", playerCount));
        ObjectInputStream objectInputStream;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
            valid = (boolean[][]) objectInputStream.readObject();



        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        tile = new ItemCard[9][9];
    }

    public boolean isValid(int row, int col) {
        return valid[row][col];
    }


    public ItemCard peekCard(int row, int col) {
        return tile[row][col];
    }

    public ItemCard popCard(int row, int col) {
        ItemCard card = tile[row][col];
        tile[row][col] = null;
        return card;
    }

    public void setTile(ItemCard tile, int row, int col) {
        this.tile[row][col] = tile;
    }

    public ItemCard[][] getTile() {
        return tile;
    }
}
