package it.polimi.ingsw.model.ItemCards;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class that represents the Item Cards.
 */
public class ItemCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -648130833358549189L;
    private final ItemType type;
    private final int sprite;

    /**
     * Creates a new Item Card with the provided type and sprite.
     *
     * @param type   the type of the item card.
     * @param sprite must be between 0 and 2.
     */
    public ItemCard(ItemType type, int sprite) {
        this.type = type;
        if (sprite < 0 || sprite > 2)
            throw new IllegalArgumentException("Sprite must be between 0 and 2");
        this.sprite = sprite;
    }

    /**
     * @return the type of this Item Card.
     */
    public ItemType getType() {
        return type;
    }

    /**
     * @return the sprite of this Item Card.
     */
    public int getSprite() {
        return sprite;
    }

    /**
     * @return the abbreviation of this Item Card
     */
    @Override
    public String toString() {
        return type.getAbbreviation();
    }
}
