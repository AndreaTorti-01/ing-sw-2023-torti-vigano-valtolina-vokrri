package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.ItemCard;
import it.polimi.ingsw.model.ItemType;
import it.polimi.ingsw.model.Shelf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
    static public Shelf getShelfFromFile(String fileName) {
        ItemCard[][] pattern = new ItemCard[Constants.numberOfRows][Constants.numberOfColumns];

        try {
            InputStream inputStream = FileUtils.class.getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int column = 0; column < line.length(); column++) {
                    char currentChar = line.charAt(column);
                    if (currentChar == '*') continue;

                    // gets the type of the ItemCard given the abbreviation found in the file
                    // and inserts it in the correct position of the matrix
                    pattern[row][column] = new ItemCard(
                            ItemType.getItemTypeFromAbbreviation(currentChar)
                    );
                }

                // goes to next line
                line = reader.readLine();
                row++;
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        return new Shelf(pattern);
    }
}
