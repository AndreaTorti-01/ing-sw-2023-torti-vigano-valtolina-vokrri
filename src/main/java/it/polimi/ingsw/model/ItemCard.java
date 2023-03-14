package main.java.it.polimi.ingsw.model;


enum ItemType {
    CATS,
    GAMES,
    BOOKS,
    FRAMES,
    TROPHIES,
    PLANTS
}

public class ItemCard extends GameObject {
    private ItemType type;

    public ItemType getType() {
        return type;
    }

}
