package it.polimi.ingsw.model;

import java.io.*;

public class Board extends GameObject {
    private boolean[][] valid;
    private ItemCard[][] tile;

    public Board(int playerCount) throws FileNotFoundException {
        try {
            FileInputStream file = new FileInputStream(String.format("board%d.dat", playerCount));
                ObjectInputStream inputStream = new ObjectInputStream(file);
                valid = (boolean[][]) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // TODO:   l'import di ItemCard[][]
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
}
