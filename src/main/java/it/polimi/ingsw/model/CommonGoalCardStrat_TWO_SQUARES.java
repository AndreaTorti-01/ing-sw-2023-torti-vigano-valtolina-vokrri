package it.polimi.ingsw.model;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

public class CommonGoalCardStrat_TWO_SQUARES implements CommonGoalCardStrat {
    @Override
    public boolean checkCard(Shelf shelf) {
        int squaresNum = 0;
        int squaresToFind = 2;
        int squaresDim = 2;
        boolean validSquare;
        //north-west corner of the potential square
        int cornerNW;
        Shelf shelfCopy = shelf.getCopy();

        for (int row = 0; row < numberOfRows - squaresDim; row++) {
            for (int column = 0; column < numberOfColumns - squaresDim; column++) {
                ItemCard currentCard = shelfCopy.getCardAt(row, column);
                //if current card is null, there is no possible pattern
                if (currentCard == null) continue;

                //else, i check if it's a square
                validSquare = true;
                for (int r = row; r < row + squaresDim && validSquare; r++) {
                    for (int c = column; c < column + squaresDim && validSquare; c++) {
                        ItemCard currentSubCard = shelfCopy.getCardAt(r, c);

                        // if one of the cards that could form the square is null
                        // the square is not valid
                        if (currentSubCard == null) {
                            validSquare = false;
                            break;
                        }

                        //if a tile has a different type than the NWcorner, the square is not valid
                        if (!currentSubCard.getType().equals(currentCard.getType()))
                            validSquare = false;
                    }
                }

                if (validSquare) {
                    //increase number of found squares
                    squaresNum++;
                    //have to eliminate the whole square to be sure not to superimpose two squares
                    for (int r = row; r < row + squaresDim; r++)
                        for (int c = column; c < column + squaresDim; c++)
                            shelfCopy.setCardAt(r, c, null);
                    return squaresNum == squaresToFind;
                }
            }
        }
        return false;
    }

    @Override
    public CommonGoalCardType getType() {
        return CommonGoalCardType.TWO_SQUARES;
    }
}
