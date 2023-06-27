package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * A class that represents a Move Message.
 * This class contains the necessary information to make a move.
 */
public class MoveMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The cards chosen by the player already in order.
     */
    private final List<List<Integer>> pickedCards;
    /**
     * The column chosen by the player in which to insert the chosen cards.
     */
    private final int column;

    /**
     * Creates a new Move Message from the provided parameters.
     *
     * @param pickedCards the cards chosen by the player.
     * @param column      the column chosen by the player in which to insert the chosen cards.
     */
    public MoveMsg(List<List<Integer>> pickedCards, int column) {
        this.pickedCards = pickedCards;
        this.column = column;
    }

    /**
     * Gets the cards chosen by the player.
     *
     * @return the cards chosen by the player.
     */
    public List<List<Integer>> getPickedCards() {
        return pickedCards;
    }

    /**
     * Gets the column chosen by the player in which to insert the chosen cards.
     *
     * @return the column chosen by the player in which to insert the chosen cards.
     */
    public int getColumn() {
        return column;
    }
}
