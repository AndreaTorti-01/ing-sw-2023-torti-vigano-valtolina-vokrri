package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class MoveMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<List<Integer>> pickedCards;
    private final int column;

    public MoveMsg(List<List<Integer>> pickedCards, int column) {
        this.pickedCards = pickedCards;
        this.column = column;
    }

    public List<List<Integer>> getPickedCards() {
        return pickedCards;
    }

    public int getColumn() {
        return column;
    }
}
