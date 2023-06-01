package it.polimi.ingsw.model.ItemCards;

import java.io.Serial;
import java.io.Serializable;

public class ItemCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -648130833358549189L;
    private final ItemType type;
    private final ItemVariant variant;

    /**
     * @param type   the type of the item card, passed as an enum value
     */
    public ItemCard(ItemType type) {
        this.type = type;
        //generates a random variant
        this.variant = ItemVariant.getRandomItemVariant();
    }

    public ItemType getType() {
        return type;
    }
    public ItemVariant getVariant(){return this.variant;}

    @Override
    public String toString() {
        return type.getAbbreviation();
    }
}
