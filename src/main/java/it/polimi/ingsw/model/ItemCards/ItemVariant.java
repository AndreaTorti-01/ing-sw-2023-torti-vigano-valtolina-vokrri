package it.polimi.ingsw.model.ItemCards;

import java.io.Serializable;
import java.util.Random;

public enum ItemVariant implements Serializable {
    VARIANT_1,
    VARIANT_2,
    VARIANT_3;

    public static ItemVariant getRandomItemVariant() {
        final int numberOfVariants = ItemVariant.values().length;
        int randomIndex = new Random().nextInt(numberOfVariants);
        //randomIndex is between 0 and the number of variants -1

        return ItemVariant.values()[randomIndex];
    }
}

