package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class PersonalGoalCard {
    private final ItemType[][] pattern;

    /**
     * The file is read through a custom parser
     * <p>
     * Creates a new PersonalGoalCard and initializes the pattern with the one found in the pattern file
     * found in ./resources/personalGoalCards folder with the corresponding index.
     * <p>
     * The file name has the following structure: PGC{index}.txt (with index = number from 0 to 11 given to the constructor)
     *
     * @param index of the personalGoalCard, used to get the corresponding pattern from the file
     * @throws IndexOutOfBoundsException when given an index outside the range 0-11.
     */
    public PersonalGoalCard(int index) {
        if (index < 0 || index > Constants.numberOfPersonalGoalCardTypes - 1)
            throw new IndexOutOfBoundsException("provided index (" + index + ") is out of range 0-" + (Constants.numberOfPersonalGoalCardTypes - 1));

        // initializes the pattern to a null matrix
        pattern = new ItemType[Constants.numberOfRows][Constants.numberOfColumns];

        try {
            // gets the pattern file corresponding to the given index
            InputStream inputStream = getClass().getResourceAsStream(
                    String.format("/personalGoalCards/%dPGC.txt", index)
            );

            // allows to read data from the obtained file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int column = 0; column < line.length(); column++) {
                    char currentChar = line.charAt(column);

                    // gets the type of the ItemCard given the abbreviation found in the file
                    // and inserts it in the correct position of the matrix
                    pattern[row][column] = ItemType.getItemTypeFromAbbreviation(currentChar);
                }

                // goes to next line
                line = reader.readLine();
                row++;
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemType[][] getPattern() {
        return pattern;
    }

    public boolean checkPattern(Shelf shelf) {
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);
                ItemType checker = this.getTypeAt(row, column);

                if (currentCard != null && !currentCard.type().equals(checker)) return false;
                if (currentCard == null && checker != null) return false;
            }
        }
        return true;
    }

    public ItemType getTypeAt(int row, int column) {
        return pattern[row][column];
    }
}
