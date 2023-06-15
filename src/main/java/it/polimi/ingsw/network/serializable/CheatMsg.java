package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

public class CheatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 5419262571274903799L;
    String cheater;

    public CheatMsg(String cheater) {
        this.cheater = cheater;
    }

    public String getCheater() {
        return cheater;
    }
}
