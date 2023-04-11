package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {

    @Test
    void testEmptyShelf() {
        Shelf shelf = new Shelf();

        // empty shelf initialized with null values
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                assertNull(shelf.getCardAt(row, column));
            }
        }
    }

    @Test
    void testFullShelf() {
        ItemCard[][] itemCards = new ItemCard[Constants.numberOfRows][Constants.numberOfColumns];

        // initializes the matrix to random values
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                // creates a random itemCard
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType());

                // inserts it in the matrix
                itemCards[row][column] = randomItemCard;
            }
        }

        // creates a new shelf with the given elements
        Shelf shelf = new Shelf(itemCards);

        // checks if the elements have the same type
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                // the items in the shelf are not the same as the ones in the matrix!
                // the memory pointer should be different
                assertNotEquals(shelf.getCardAt(row, column), itemCards[row][column]);

                // the items in the shelf are not the same as the ones in the matrix,
                // but the type should be
                assertEquals(shelf.getCardAt(row, column).type(), itemCards[row][column].type());
            }
        }

    }

    @Test
    void testGetColumnLengthMethod() {
        Shelf shelf = this.initializeFullRandomShelf();

        // every column should have the maximum number of elements it can hold
        for (int column = 0; column < Constants.numberOfColumns; column++) {
            assertEquals(shelf.getColumnLength(column), Constants.numberOfRows);
        }

        shelf = new Shelf();

        // every column should have length 0
        for (int column = 0; column < Constants.numberOfColumns; column++) {
            assertEquals(shelf.getColumnLength(column), 0);
        }
    }


    @Test
    void testSetCardAtMethod() {
        Shelf shelf = new Shelf();
        Shelf shelfCopy = shelf.getCopy();

        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                // because java is needy
                final int finalRow = row;
                final int finalColumn = column;

                // creates a random itemCard
                final ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType());

                // shelf is the original, so it should not be able to use the setCardAt method
                assertThrows(
                        IllegalAccessError.class,
                        () -> shelf.setCardAt(finalRow, finalColumn, randomItemCard)
                );

                // shelfCopy is a copy of shelf, so it should be able to use the setCardAt method
                assertDoesNotThrow(
                        () -> shelfCopy.setCardAt(finalRow, finalColumn, randomItemCard)
                );
            }
        }
    }

    @Test
    void testInsertMethod() {
        Shelf shelf = this.initializeFullRandomShelf();

        // checks if the method throws error trying to insert elements in a full column
        for (int column = 0; column < Constants.numberOfColumns; column++) {
            // because java is needy
            final int finalColumn = column;

            // creates a random itemCard
            ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType());

            // should not insert elements in a full column
            assertThrows(
                    IndexOutOfBoundsException.class,
                    () -> shelf.insert(finalColumn, randomItemCard)
            );
        }
    }

    @Test
    void testGetCopyMethod() {
        Shelf shelf = this.initializeFullRandomShelf();

        // checks if the copy of the shelf has the same values inside
        Shelf shelfCopy = shelf.getCopy();
        for (int row = 0; row < Constants.numberOfRows; row++) {
            for (int column = 0; column < Constants.numberOfColumns; column++) {
                ItemCard actual = shelfCopy.getCardAt(row, column);
                ItemCard expected = shelf.getCardAt(row, column);

                // the itemCards inside the shelves are NOT the same!
                // the memory pointer should be different
                assertNotEquals(actual, expected);

                // the itemCards inside the shelves are NOT the same,
                // but the type should be
                assertEquals(actual.type(), expected.type());
            }
        }
    }

    /**
     * Initializes a new <b>full</b> Shelf with random values
     *
     * @return a shelf with random values
     */
    private Shelf initializeFullRandomShelf() {
        Shelf shelf = new Shelf();

        for (int column = 0; column < Constants.numberOfColumns; column++) {
            for (int row = 0; row < Constants.numberOfRows; row++) {
                // creates a random itemCard
                ItemCard randomItemCard = new ItemCard(ItemType.getRandomItemType());

                // inserts it in the shelf
                shelf.insert(column, randomItemCard);
            }
        }

        return shelf;
    }

}