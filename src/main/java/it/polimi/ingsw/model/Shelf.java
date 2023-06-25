package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing the Shelf.
 * This class contains all the Item Cards inserted by the Player.
 */
public class Shelf implements Serializable {
    @Serial
    private static final long serialVersionUID = 1599653120498179003L;
    /**
     * The Item Cards inside the shelf.
     */
    private final ItemCard[][] items;
    /**
     * True if this shelf is a copy of another one.
     */
    private boolean isACopy;

    /**
     * Creates a new empty Shelf.
     * <p>
     * The (0, 0) position is the top left corner.
     */
    public Shelf() {
        this.items = new ItemCard[numberOfRows][numberOfColumns];
    }

    /**
     * Creates a new Shelf based on the matrix of ItemCard passed as argument
     *
     * @param items a matrix of Item Cards that represents the shelf
     */
    public Shelf(ItemCard[][] items) {
        // initializes items to a null matrix of
        // Constants.numberOfRows rows and Constants.numberOfColumns columns
        this();

        // deep copies the elements from the matrix passed as argument to the constructor
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = items[row][column];

                if (currentCard == null) continue;
                // creates a new itemCard with the same type as the one currently selected
                // and inserts it in the current position of the new shelf
                this.items[row][column] = new ItemCard(currentCard.getType(), 0);
            }
        }
    }

    /**
     * @param row    must be between boundaries (provided in the {@link Constants} file)
     * @param column must be between boundaries (provided in the {@link Constants} file)
     * @return ItemCard at the provided position (if any), null otherwise.
     */
    public ItemCard getCardAt(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row > numberOfRows) {
            throw new IllegalArgumentException(
                    "Provided row (" + row + ") is out of range 0 - " + numberOfRows
            );
        }

        if (column < 0 || column > numberOfColumns) {
            throw new IllegalArgumentException(
                    "Provided column (" + column + ") is out of range 0 - " + numberOfColumns
            );
        }

        return items[row][column];
    }

    /**
     * Sets the card at the provided position to the provided value.
     * <p>
     * <p>
     * ************************************* <p>
     * * USE ONLY ON A CLONE OF THE SHELF! * <p>
     * ************************************* <p>
     *
     * @param row     must be between boundaries (provided in the {@link Constants} file).
     * @param column  must be between boundaries (provided in the {@link Constants} file).
     * @param newCard the new value assigned to be set.
     */
    public void setCardAt(int row, int column, ItemCard newCard) throws IllegalAccessError {
        if (!this.isACopy) throw new IllegalAccessError("Cannot call this method on the original shelf!");
        this.items[row][column] = newCard;
    }

    // TODO keep? bad practice
    public ItemCard[][] getItems() {
        return items;
    }

    /**
     * @return a deep copy of the current Shelf.
     */
    public Shelf getCopy() {
        // initializes a new null matrix of ItemCards
        ItemCard[][] itemCards = new ItemCard[numberOfRows][numberOfColumns];

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = this.getCardAt(row, column);

                // the matrix is already initialized to null
                if (currentCard == null) continue;

                // creates a new ItemCard with the same type as the one currently selected
                // and inserts it in the itemCards at the same position
                itemCards[row][column] = new ItemCard(currentCard.getType(), 0);
            }
        }

        // creates a new shelf with the same arrangement
        Shelf shelfCopy = new Shelf(itemCards);
        shelfCopy.isACopy = true; // !important

        return shelfCopy;
    }

    /**
     * Inserts the given ItemCard in the first available row of the provided column in the shelf.
     *
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @param item   the card to be inserted.
     * @throws IndexOutOfBoundsException if the column is full.
     */
    public void insert(int column, ItemCard item) throws IndexOutOfBoundsException {
        if (items[0][column] != null)
            throw new IndexOutOfBoundsException("Cannot insert elements in a full column!");
        else {
            int row = numberOfRows - 1;
            while (items[row][column] != null)
                row--;
            items[row][column] = item;
        }
    }

    /**
     * @param column must be between boundaries (provided in the {@link Constants} file).
     * @return length of the provided column.
     */
    public int getColumnLength(int column) {
        int length = 0;
        for (int row = 0; row < numberOfRows; row++) {
            if (items[row][column] != null) length++;
        }

        return length;
    }

    /**
     * @return true if the shelf does not have any available space left, false otherwise.
     */
    public boolean isFull() {
        for (int column = 0; column < numberOfColumns; column++) {
            // if a column does not have a card in the first row, the shelf is not full
            if (this.getCardAt(0, column) == null) return false;
        }
        return true;
    }

    /**
     * Fills the current shelf with random Item Cards
     */
    public void fill() {
        if (this.isFull()) return;

        for (int column = 0; column < numberOfColumns; column++) {
            while (this.getColumnLength(column) != numberOfRows) {
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType(), 1);
                this.insert(column, randomItemCard);
            }
        }
    }

    /**
     * @return a string representation of this shelf.
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (this.getCardAt(row, column) == null) output.append("* ");
                else output.append(this.getCardAt(row, column).toString()).append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }
}
