package it.polimi.ingsw.model;

public class Board extends GameObject{
    private boolean[][] valid;
    private ItemCard[][] tile;

    public Board(){
        throw new UnsupportedOperationException("TO DO");
        //need ItemCard constructor to define the method
    };

    public boolean isValid(int row, int col){
        throw new UnsupportedOperationException("TO DO");
    };

    public ItemCard peekCard(int row, int col){
        throw new UnsupportedOperationException("TO DO");
    };

    public ItemCard popCard(int row, int col){
        throw new UnsupportedOperationException("TO DO");
    };

}
