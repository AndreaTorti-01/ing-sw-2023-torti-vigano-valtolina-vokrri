package it.polimi.ingsw.model;


public class  ItemCard extends GameObject {
    private ItemType type;
    public ItemCard(ItemType type) {
        this.type = type;
    }
    public ItemType getType() {
        return type;
    }

}
