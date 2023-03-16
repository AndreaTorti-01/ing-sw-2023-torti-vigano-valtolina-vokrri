package it.polimi.ingsw.model;

public class Shelf extends GameObject {
    private ItemCard[][] items;


    public Shelf() {

        items = new ItemCard[6][5];

    }

    public ItemCard getCard(int row, int col) {
        return items[row][col];
    }

    public ItemCard[][] getShelf() {
        return items;
    }

    public void insert(int col, ItemCard item) {
        throw new UnsupportedOperationException("TO DO");
    }
}
