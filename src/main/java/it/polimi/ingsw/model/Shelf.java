package it.polimi.ingsw.model;

public class Shelf extends GameObject {
    private ItemCard[][] items;


    public Shelf() {

        items = new ItemCard[6][5];
        for (int i = 0; i < 6; i++)
            for(int j = 0; j < 5; j++)
                items[i][j] = null;

    }

    public ItemCard getCard(int row, int col) {
        return items[row][col];
    }

    public ItemCard[][] getShelf() {
        return items;
    }

    public void insert(int col, ItemCard item) throws RuntimeException {
        if(items[0][col] != null)
            throw new RuntimeException("COLONNA PIENA");
        else {
            int i = 5;
            while (items[i][col] != null)
                i--;
            items[i][col] = item;
        }
    }
}
