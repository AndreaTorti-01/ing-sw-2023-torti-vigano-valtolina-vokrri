package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A factory class that creates Personal Goal Cards.
 */
public class PersonalGoalCardFactory {
    /**
     * Creates a new Personal Goal Card by parsing a well formatted file that contains its pattern.<p>
     * The file has the following name: {@code PGC-{index}.txt}, where {@code index} is a number between 0-11,
     * corresponding to a specific pattern.
     *
     * @param index the index of the file containing the pattern to be parsed.
     * @return the Personal Goal Card containing the parsed pattern.
     */
    public PersonalGoalCard createPersonalGoalCard(int index) {
        if (index < 0 || index > Constants.numberOfPersonalGoalCardTypes - 1)
            throw new IndexOutOfBoundsException("provided index (" + index + ") is out of range 0-" + (Constants.numberOfPersonalGoalCardTypes - 1));

        PersonalGoalCard personalGoalCard = new PersonalGoalCard();
        
        try {
            // gets the pattern file corresponding to the given index
            InputStream inputStream = getClass().getResourceAsStream(String.format("/personalGoalCards/%dPGC.txt", index));

            // allows to read data from the obtained file
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int column = 0; column < line.length(); column++) {
                    char currentChar = line.charAt(column);

                    // gets the type of the ItemCard given the abbreviation found in the file
                    // and inserts it in the correct position of the matrix
                    personalGoalCard.setTileAt(row, column, ItemType.getItemTypeFromAbbreviation(currentChar));
                }

                // goes to next line
                line = reader.readLine();
                row++;
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        return personalGoalCard;
    }
}
