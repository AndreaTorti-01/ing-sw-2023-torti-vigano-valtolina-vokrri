package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

public class ChatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String destPlayer;
    private final String sourcePlayer;
    private final boolean isMsgPublic;
    private final String message;

    public ChatMsg(String destPlayer, String sourcePlayer, boolean isMsgPublic, String message) {
        this.destPlayer = destPlayer;
        this.sourcePlayer = sourcePlayer;
        this.isMsgPublic = isMsgPublic;
        this.message = message;
    }

    public String getDestPlayer() {
        return destPlayer;
    }

    public String getSourcePlayer() {
        return sourcePlayer;
    }

    public boolean isMsgPublic() {
        return isMsgPublic;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return sourcePlayer + ": " + message;
    }
}
