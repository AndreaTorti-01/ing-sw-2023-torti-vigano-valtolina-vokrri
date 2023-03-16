package it.polimi.ingsw.model;


import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PersonalGoalCard extends GameObject {
    private final ItemType[][] pattern;

    /**
     * creates the pattern from the file with corresponding index.
     * file name: PGC{index}.txt
     *
     * @param index index of the PersonalGoalCard
     */
    public PersonalGoalCard(int index) {
        pattern = new ItemType[6][5];
        String fileName = String.format("personalGoalCards/PGC%d.txt", index);
        try {
            Scanner scanner = new Scanner(new File(fileName));

            String line = scanner.nextLine();
            int row = 0;
            while (line != null) {
                char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char currentChar = chars[i];
                    if (currentChar != '*') {
                        pattern[row][i] = ItemType.valueOf(
                                String.valueOf(ItemType.getItemTypeFromAbbreviation(currentChar))
                        );
                    } else pattern[row][i] = null;
                }
                line = scanner.nextLine();
                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ItemType[][] getPattern() {
        return pattern;
    }
}
