package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing a Personal Goal Card.
 * This class contains a pattern to be satisfied by the player that owns this card.
 */
public class PersonalGoalCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -8049860441030982905L;
    /**
     * The pattern of this Personal Goal Card.
     */
    private final ItemType[][] pattern;
    /**
     * The id of this Personal Goal Card.
     */
    private int cardID;

    /**
     * Creates a new Personal Goal Card and initializes its pattern to a null matrix.
     */
    public PersonalGoalCard() {
        // initializes the pattern to a null matrix
        pattern = new ItemType[Constants.numberOfRows][Constants.numberOfColumns];
    }

    /**
     * Gets the id of this Personal Goal Card.
     *
     * @return the id of this Personal Goal Card.
     */
    public int getID() {
        return cardID;
    }

    /**
     * Sets the id of this Personal Goal Card to the provided one.
     *
     * @param id the id to be set.
     */
    public void setID(int id) {
        cardID = id;
    }

    /**
     * Gets the pattern of this Personal Goal Card.
     *
     * @return the pattern of this Personal Goal Card.
     */
    public ItemType[][] getPattern() {
        return pattern;
    }

    /**
     * Sets the tile at the provided position to the provided Item Card.
     *
     * @param row      must be between boundaries (provided in the {@link Constants} file).
     * @param column   must be between boundaries (provided in the {@link Constants} file).
     * @param itemType the Item Card to be inserted at the provided position.
     * @throws IllegalArgumentException if the provided position is not valid.
     */
    public void setTileAt(int row, int column, ItemType itemType) throws IllegalArgumentException {
        if (row < 0 || row >= numberOfRows) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + numberOfRows
            );
        }

        if (column < 0 || column >= numberOfColumns) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + numberOfColumns
            );
        }

        pattern[row][column] = itemType;
    }

    /**
     * Checks if the pattern of the Personal Goal Card is satisfied in the provided shelf.
     *
     * @param shelf the shelf to check the pattern in.
     * @return the points obtained by completing the pattern.
     */
    public int checkPattern(Shelf shelf) {
        int numberOfGoodTiles = 0;

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);
                ItemType checker = this.getTypeAt(row, column);

                if (currentCard != null && currentCard.getType().equals(checker)) numberOfGoodTiles++;
            }
        }
        return switch (numberOfGoodTiles) {
            case 1 -> 1;
            case 2 -> 2;
            case 3 -> 4;
            case 4 -> 6;
            case 5 -> 9;
            case 6 -> 12;
            default -> 0;
        };
    }

    /**
     * Gets the type of the Item Card at the provided position.
     *
     * @param row    must be between boundaries (provided in the {@link Constants} file).
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return the type of the Item Card at the provided position.
     */
    public ItemType getTypeAt(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= numberOfRows) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + numberOfRows
            );
        }

        if (column < 0 || column >= numberOfColumns) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + numberOfColumns
            );
        }
        return pattern[row][column];
    }

    /**
     * Gets a string representation of this Common Goal Card.
     *
     * @return a string representation of this Common Goal Card.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (this.getTypeAt(i, j) == null) output.append("* ");
                else output.append(this.getTypeAt(i, j).getAbbreviation()).append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }

}
