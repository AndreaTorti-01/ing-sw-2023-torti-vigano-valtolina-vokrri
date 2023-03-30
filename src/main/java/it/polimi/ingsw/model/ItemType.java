package it.polimi.ingsw.model;

import java.util.NoSuchElementException;

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
        for (ItemType type : ItemType.values()) {
            if (type.toString().charAt(0) == abbreviation) return type;
        }

        throw new NoSuchElementException("No item type found with the given abbreviation " + abbreviation);
    }
}
