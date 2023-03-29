package it.polimi.ingsw.model;

public class Shelf extends GameObject {
    private final ItemCard[][] items;

    /**
     * The shelf is empty when instantiated. 0,0 is the top left corner
     */
    public Shelf() {
        items = new ItemCard[6][5];
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
            int i = 5;
            while (items[i][column] != null)
                i--;
            items[i][column] = item;
        }
    }
}
