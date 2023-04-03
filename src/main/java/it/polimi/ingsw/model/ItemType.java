package it.polimi.ingsw.model;

import java.util.NoSuchElementException;
import java.util.Random;

public enum ItemType {
    CATS('C'),
    GAMES('G'),
    BOOKS('B'),
    FRAMES('F'),
    TROPHIES('T'),
    PLANTS('P');

    ItemType(char abbreviation) {
    }

    /**
     * Return the ItemType from the abbreviation
     *
     * @param abbreviation the abbreviation of the ItemType
     * @return the ItemType
     */
    public static ItemType getItemTypeFromAbbreviation(char abbreviation) {
        if (abbreviation == '*') return null;

        for (ItemType type : ItemType.values()) {
            if (type.toString().charAt(0) == abbreviation) return type;
        }

        throw new NoSuchElementException("No item type found with the given abbreviation " + abbreviation);
    }

    /**
     * Returns a random value from the one in the enumeration.
     * For Test purpose.
     *
     * @return a random value
     */
    public static ItemType getRandomItemType() {
        final int numberOfItems = ItemType.values().length;
        int randomIndex = new Random().nextInt(numberOfItems);

        return ItemType.values()[randomIndex];
    }
}
