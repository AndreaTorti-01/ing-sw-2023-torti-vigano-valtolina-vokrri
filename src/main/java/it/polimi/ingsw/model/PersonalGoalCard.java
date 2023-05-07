package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;

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
     * @throws IndexOutOfBoundsException when given an index outside the range 0-11.
     */
    public PersonalGoalCard() {
        // initializes the pattern to a null matrix
        pattern = new ItemType[Constants.numberOfRows][Constants.numberOfColumns];
    }

    public ItemType[][] getPattern() {
        return pattern;
    }

    public void setPatternTile(int row, int column, ItemType itemType) {
        pattern[row][column] = itemType;
    }

    public boolean checkPattern(Shelf shelf) {
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                ItemCard currentCard = shelf.getCardAt(row, column);
                ItemType checker = this.getTypeAt(row, column);

                if (currentCard != null && !currentCard.getType().equals(checker)) return false;
                if (currentCard == null && checker != null) return false;
            }
        }
        return true;
    }

    public ItemType getTypeAt(int row, int column) {
        return pattern[row][column];
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                if (this.getTypeAt(i, j) == null) output.append("* ");
                else output.append(this.getTypeAt(i, j).getAbbreviation()).append(" ");
            }
            output.append("\n");
        }

        return output.toString();
    }
}
