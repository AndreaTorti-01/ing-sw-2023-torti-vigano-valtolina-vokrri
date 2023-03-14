package main.java.it.polimi.ingsw.model;

public class Shelf extends GameObject {
    private ItemCard[][] items;


    public Shelf() {
        throw new UnsupportedOperationException("TO DO");
        /*
        items = new ItemCard[6][5];
        for(int i = 0; i < 6; i++)
            for(int j = 0; j < 5; j++)
                items

         */    // need ItemCard constructor to define the method
    }

    public ItemCard getCard(int row, int col) {
        return items[row][col];
    }

    //in the UML diagram the return type is ItemCard(?)
    //but ItemCard should probably be a parameter
    public void insert(int col, ItemCard item) {
        throw new UnsupportedOperationException("TO DO");
    }
}
