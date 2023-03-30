package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

public class Shelf extends GameObject {
    private final ItemCard[][] items;

    /**
     * The shelf is empty when instantiated. 0,0 is the top left corner
     */
    public Shelf() {
        items = new ItemCard[Constants.numberOfRows][Constants.numberOfColumns];
    }

    /**
     * Get the card at the specified position
     *
     * @param row    must be between 0 and 5
     * @param column must be between 0 and 4
     * @return ItemCard | null, depending on the presence of a card at the specified position
     */
    public ItemCard getCardAt(int row, int column) {
        return items[row][column];
    }

    /**
     * Get the whole shelf
     *
     * @return ItemCard[][], the whole shelf
     */
    public ItemCard[][] getShelf() {
        return items;
    }

    /**
     * Insert a card in the shelf from the top (0,0 is the top left corner)
     *
     * @param column must be between 0 and 4
     * @param item   the card to be inserted
     * @throws RuntimeException if the column is full
     */
    public void insert(int column, ItemCard item) throws RuntimeException {
        if (items[0][column] != null)
            throw new RuntimeException("Column is full");
        else {
            int i = Constants.numberOfColumns;
            while (items[i][column] != null)
                i--;
            items[i][column] = item;
        }
    }

    /**
     * Returns the length of the specified column of the shelf
     *
     * @param column to calculate the length of
     * @return length of the column
     */
    public int getColumnLength(int column) {
        int length = 0;

        for (int row = 0; row < Constants.numberOfRows; row++) {
            if (items[row][column] != null) length++;
        }

        return length;
    }
}
