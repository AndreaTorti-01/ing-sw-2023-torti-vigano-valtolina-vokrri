package it.polimi.ingsw.model;

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
        return switch (abbreviation) {
            case 'C' -> ItemType.CATS;
            case 'G' -> ItemType.GAMES;
            case 'B' -> ItemType.BOOKS;
            case 'F' -> ItemType.FRAMES;
            case 'T' -> ItemType.TROPHIES;
            default -> ItemType.PLANTS;
        };
    }

}
