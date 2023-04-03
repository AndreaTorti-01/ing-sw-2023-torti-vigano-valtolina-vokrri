package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

public class Shelf extends GameObject {
    private final ItemCard[][] items;
    private boolean isACopy;

    /**
     * The shelf is empty when instantiated. 0,0 is the top left corner
     */
    public Shelf() {
        this.items = new ItemCard[Constants.numberOfRows][Constants.numberOfColumns];
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
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                ItemCard currentCard = items[row][column];

                if (currentCard == null) continue;
                // creates a new itemCard with the same type as the one currently selected
                // and inserts it in the current position of the new shelf
                this.items[row][column] = new ItemCard(currentCard.getType());
            }
        }
    }

    /**
     * Get the card at the specified position
     *
     * @param row    must be between boundaries (specified in the Constants.java file)
     * @param column must be between boundaries (specified in the Constants.java file)
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
     * * USE ONLY FOR A CLONE OF THE SHELF! * <p>
     * ************************************** <p>
     *
     * @param row     must be between boundaries (specified in the Constants.java file)
     * @param column  must be between boundaries (specified in the Constants.java file)
     * @param newCard the new value
     */
    public void setCardAt(int row, int column, ItemCard newCard) throws IllegalAccessError {
        if (!this.isACopy) throw new IllegalAccessError("Cannot call this method on the original shelf!");
        this.items[row][column] = newCard;
    }

    /**
     * Creates a copy of the current shelf and returns it
     *
     * @return a copy of the current Shelf
     */
    public Shelf getCopy() {
        // initializes a new null matrix of ItemCards
        ItemCard[][] itemCards = new ItemCard[Constants.numberOfRows][Constants.numberOfColumns];

        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                ItemCard currentCard = this.getCardAt(row, column);

                // the matrix is already initialized to null
                if (currentCard == null) continue;

                // creates a new ItemCard with the same type as the one currently selected
                // and inserts it in the itemCards at the same position
                itemCards[row][column] = new ItemCard(currentCard.getType());
            }
        }

        // creates a new shelf with the same arrangement
        Shelf shelfCopy = new Shelf(itemCards);
        shelfCopy.isACopy = true; // !important

        return shelfCopy;
    }

    /**
     * Insert a card in the shelf from the top (0,0 is the top left corner)
     *
     * @param column must be between 0 and 4
     * @param item   the card to be inserted
     * @throws RuntimeException if the column is full
     */
    public void insert(int column, ItemCard item) throws RuntimeException {
        if (items[0][column] != null)
            throw new RuntimeException("Column is full");
        else {
            int row = Constants.numberOfRows;
            while (items[row][column] != null)
                row--;
            items[row][column] = item;
        }
    }

    /**
     * Returns the length of the specified column of the shelf
     *
     * @param column to calculate the length of
     * @return length of the column
     */
    public int getColumnLength(int column) {
        int length = 0;
        for (int row = 0; row < Constants.numberOfRows; row++) {
            if (items[row][column] != null) length++;
        }

        return length;
    }
}
