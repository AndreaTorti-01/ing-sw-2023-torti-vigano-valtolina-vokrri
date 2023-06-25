package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class that represents a Cheat Message.
 */
public class CheatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 5419262571274903799L;
    /**
     * The player who cheated.
     */
    String cheater;

    /**
     * Creates a new Cheat Message from the provided cheater.
     *
     * @param cheater the player who sent this message.
     */
    public CheatMsg(String cheater) {
        this.cheater = cheater;
    }

    /**
     * @return the name of the player who created the message.
     */
    public String getCheater() {
        return cheater;
    }
}
