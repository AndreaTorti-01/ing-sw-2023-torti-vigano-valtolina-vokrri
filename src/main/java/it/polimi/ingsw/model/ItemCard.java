package it.polimi.ingsw.model;

public enum ItemType {
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
