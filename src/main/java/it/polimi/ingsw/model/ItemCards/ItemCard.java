package it.polimi.ingsw.model.ItemCards;

public final class ItemCard {
    private final ItemType type;
    private final int sprite;

    /**
     * @param type   the type of the item card, passed as an enum value
     * @param sprite must be between 0 and 2
     */
    public ItemCard(ItemType type, int sprite) {
        this.type = type;
        if (sprite < 0 || sprite > 2)
            throw new IllegalArgumentException("Sprite must be between 0 and 2");
        this.sprite = sprite;
    }

    public ItemType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getAbbreviation();
    }

    public int getSprite() {
        return sprite;
    }
}
