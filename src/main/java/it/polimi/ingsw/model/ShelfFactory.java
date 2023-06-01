package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShelfFactory {
    public Shelf createShelf(String fileName) {
        Shelf shelf = new Shelf();
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(fileName);
            assert inputStream != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            int row = 0;
            String line = reader.readLine();
            while (line != null) {
                for (int column = 0; column < line.length(); column++) {
                    char currentChar = line.charAt(column);
                    if (currentChar == '*') continue;

                    // gets the type of the ItemCard given the abbreviation found in the file
                    // and inserts it in the correct position of the matrix
                    shelf.setCardAt(row, column, new ItemCard(ItemType.getItemTypeFromAbbreviation(currentChar)));
                }

                // goes to next line
                line = reader.readLine();
                row++;
            }
        } catch (IOException | NullPointerException e) {
            throw new RuntimeException(e);
        }
        return shelf;
    }
}
