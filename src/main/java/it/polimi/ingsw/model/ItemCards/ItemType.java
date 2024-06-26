package it.polimi.ingsw.model.ItemCards;

import java.util.NoSuchElementException;
import java.util.Random;

/**
 * An enumeration of the types of Item Cards.
 */
public enum ItemType {
    /**
     * Cat item type.
     */
    CATS('C'),
    /**
     * Game item type.
     */
    GAMES('G'),
    /**
     * Book item type.
     */
    BOOKS('B'),
    /**
     * Frames item type.
     */
    FRAMES('F'),
    /**
     * Trophies item type.
     */
    TROPHIES('T'),
    /**
     * Plants item type.
     */
    PLANTS('P');

    /**
     * Creates a new ItemType with the provided abbreviation.
     *
     * @param abbreviation the abbreviation of the ItemType
     */
    ItemType(char abbreviation) {
    }

    /**
     * Gets the ItemType from the provided abbreviation.
     *
     * @param abbreviation the abbreviation of the ItemType
     * @return the ItemType given its abbreviation
     */
    public static ItemType getItemTypeFromAbbreviation(char abbreviation) {
        if (abbreviation == '*') return null;

        for (ItemType type : ItemType.values()) {
            if (type.toString().charAt(0) == abbreviation) return type;
        }

        throw new NoSuchElementException("No item type found with the given abbreviation " + abbreviation);
    }

    /**
     * ***************************<p>
     * * FOR TEST PURPOSES ONLY! *<p>
     * ***************************<p>
     *
     * @return a random item type.
     */
    public static ItemType getRandomItemType() {
        final int numberOfItems = ItemType.values().length;
        int randomIndex = new Random().nextInt(numberOfItems);

        return ItemType.values()[randomIndex];
    }

    /**
     * The abbreviation consists of the first character in the Item Type name.
     *
     * @return the first abbreviation of the Item Type.
     */
    public String getAbbreviation() {
        return String.valueOf(this.toString().charAt(0));
    }
}
