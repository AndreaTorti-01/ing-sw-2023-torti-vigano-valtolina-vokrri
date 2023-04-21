package it.polimi.ingsw.model.commonGoalCardStrats;

import it.polimi.ingsw.model.CommonGoalCardStrat;
import it.polimi.ingsw.model.ItemType;
import it.polimi.ingsw.model.Shelf;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_RAINBOWS implements CommonGoalCardStrat {

    private final int numOfRowsToFind;  // number of rainbow rows
    private final int numOfColsToFind; // number of rainbow columns
    private final CommonGoalCardType type;

    public CommonGoalCardStrat_RAINBOWS(CommonGoalCardType cardType) throws RuntimeException {
        type = cardType;
        if (cardType.equals(CommonGoalCardType.TWO_RAINBOW_COLUMNS)) {
            numOfRowsToFind = 0;
            numOfColsToFind = 2;
        } else if (cardType.equals(CommonGoalCardType.TWO_RAINBOW_LINES)) {
            numOfRowsToFind = 2;
            numOfColsToFind = 0;
        } else throw new RuntimeException("WRONG COMMONCARD TYPE");
    }

    public boolean checkPattern(Shelf slf) {
        int foundCols = 0;
        int foundRows = 0;
        List<ItemType> types = new ArrayList<>();

        // checking if the rows are rainbow
        for (int i = 0; i < numberOfRows && foundRows < numOfRowsToFind; i++) {
            boolean validRow = true;

            for (int j = 0; j < numberOfColumns && validRow; j++) {
                if (slf.getCardAt(i, j) != null && !types.contains(slf.getCardAt(i, j).getType()))
                    types.add(slf.getCardAt(i, j).getType());
                else
                    validRow = false;
            }

            if (validRow) foundRows++;
            types.clear();
        }

        // checking if the cols are rainbow
        for (int j = 0; j < numberOfColumns && foundCols < numOfColsToFind; j++) {
            boolean validCol = true;

            for (int i = 0; i < numberOfRows && validCol; i++) {
                if (slf.getCardAt(i, j) != null && !types.contains(slf.getCardAt(i, j).getType()))
                    types.add(slf.getCardAt(i, j).getType());
                else
                    validCol = false;
            }

            if (validCol) foundCols++;
            types.clear();
        }

        return (foundRows >= numOfRowsToFind) && (foundCols >= numOfColsToFind);
    }

    public CommonGoalCardType getType() {
        return type;
    }
}
