package it.polimi.ingsw.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PersonalGoalCard extends GameObject {
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
        if (index < 0 || index > 11)
            throw new IndexOutOfBoundsException("provided index (" + index + ") is out of range 0 - 11");

        // initializes the pattern to a null matrix of size 6x5
        pattern = new ItemType[6][5];

        try {
            // gets the pattern file corresponding to the given index
            InputStream inputStream = getClass().getResourceAsStream(
                    String.format("/personalGoalCards/PGC%d.txt", index)
            );

            // allows to read data from the obtained file
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int col = 0; col < line.length(); col++) {
                    char currentChar = line.charAt(col);
                    if (currentChar != '*') {
                        // gets the type of the ItemCard given the abbreviation found in the file
                        // and inserts it in the correct position of the matrix
                        pattern[row][col] = ItemType.valueOf(
                                String.valueOf(ItemType.getItemTypeFromAbbreviation(currentChar))
                        );
                    }
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

    public ItemType getTypeAt(int i, int j) {
        return pattern[i][j];
    }
}
