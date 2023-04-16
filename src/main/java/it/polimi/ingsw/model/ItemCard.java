package it.polimi.ingsw.model;

public final class ItemCard {
    private final ItemType type;

    public ItemCard(ItemType type) {
        this.type = type;
    }

    public ItemType type() {
        return type;
    }

    @Override
    public String toString() {
        return type.getAbbreviation();
    }
}
