package it.polimi.ingsw.model.commonGoalCards;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.model.Shelf;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.utils.Constants.numberOfColumns;
import static it.polimi.ingsw.utils.Constants.numberOfRows;

/**
 * A class representing the Common Goal Card Strategy.
 * Used as a strategy pattern to retrieve a specific Common Goal Card.
 */
public interface CommonGoalCardStrat extends Serializable {
    /**
     * Checks if the provided shelf matches the pattern of the specific Common Goal Card.
     *
     * @param shelf the shelf to check the pattern in.
     * @return true if the pattern is satisfied, false otherwise.
     */
    boolean checkPattern(Shelf shelf);

    /**
     * Retrieves the type of the Common Goal Card.
     *
     * @return the type of the Common Goal Card.
     */
    CommonGoalCardType getType();

}
