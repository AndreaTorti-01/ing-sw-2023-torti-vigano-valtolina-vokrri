package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class Shelf {
    private final ItemCard[][] items;
    private boolean isACopy;

    /**
     * Creates a new empty Shelf.
     * <p>
     * The (0, 0) position is the top left corner.
     */
    public Shelf() {
        this.items = new ItemCard[numberOfRows][numberOfColumns];
        this.isACopy = false;
    }

    /**
     * Creates a new Shelf based on the matrix of ItemCard passed as argument
     *
     * @param items a matrix of ItemCard that represents the shelf
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
                this.items[row][column] = new ItemCard(currentCard.type(), 0);
            }
        }
    }

    /**
     * Creates a new Shelf from the given file containing a representation of an ItemCards matrix
     *
     * @param fileName the name of the file containing a matrix of ItemCards
     */
    public Shelf(String fileName) {
        // initializes items to a null matrix of
        // Constants.numberOfRows rows and Constants.numberOfColumns columns
        this();

        try {
            InputStream inputStream = this.getClass().getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int column = 0; column < line.length(); column++) {
                    char currentChar = line.charAt(column);
                    if (currentChar == '*') continue;

                    // gets the type of the ItemCard given the abbreviation found in the file
                    // and inserts it in the correct position of the matrix
                    this.items[row][column] = new ItemCard(
                            ItemType.getItemTypeFromAbbreviation(currentChar),
                            0);
                }

                // goes to next line
                line = reader.readLine();
                row++;
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        this.isACopy = false;
    }

    /**
     * Get the card at the specified position
     *
     * @param row    must be between boundaries (specified in the {@link Constants} file)
     * @param column must be between boundaries (specified in the {@link Constants} file)
     * @return ItemCard | null, depending on the presence of a card at the specified position
     */
    public ItemCard getCardAt(int row, int column) {
        return items[row][column];
    }

    /**
     * Sets the card at the specified position to the new specified value
     * <p>
     * <p>
     * ************************************** <p>
     * * USE ONLY ON A CLONE OF THE SHELF! * <p>
     * ************************************** <p>
     *
     * @param row     must be between boundaries (specified in the {@link Constants} file)
     * @param column  must be between boundaries (specified in the {@link Constants} file)
     * @param newCard the new value assigned to the specified position
     */
    public void setCardAt(int row, int column, ItemCard newCard) throws IllegalAccessError {
        if (!this.isACopy) throw new IllegalAccessError("Cannot call this method on the original shelf!");
        this.items[row][column] = newCard;
    }

    // TODO keep?
    public ItemCard[][] getItemsMatrix() {
        return items;
    }

    /**
     * @return a deep copy of the current Shelf
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
                itemCards[row][column] = new ItemCard(currentCard.type(), 0);
            }
        }

        // creates a new shelf with the same arrangement
        Shelf shelfCopy = new Shelf(itemCards);
        shelfCopy.isACopy = true; // !important

        return shelfCopy;
    }

    /**
     * Inserts the given ItemCard in the first available row of the shelf from the top
     *
     * @param column must be between boundaries (specified in the {@link Constants} file)
     * @param item   the card to be inserted
     * @throws RuntimeException if the column is full
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
     * @param column must be between boundaries (specified in the {@link Constants} file)
     * @return length of the given column
     */
    public int getColumnLength(int column) {
        int length = 0;
        for (int row = 0; row < numberOfRows; row++) {
            if (items[row][column] != null) length++;
        }

        return length;
    }

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
