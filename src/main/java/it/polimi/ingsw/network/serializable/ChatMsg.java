package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

public class ChatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int destPlayer;
    private final int sourcePlayer;
    private final boolean isMsgPublic;

    private final String message;

    public ChatMsg(int destPlayer, int sourcePlayer, boolean isMsgPublic, String message) {
        this.destPlayer = destPlayer;
        this.sourcePlayer = sourcePlayer;
        this.isMsgPublic = isMsgPublic;
        this.message = message;
    }

    public int getDestPlayer() {
        return destPlayer;
    }

    public int getSourcePlayer() {
        return sourcePlayer;
    }

    public boolean isMsgPublic() {
        return isMsgPublic;
    }

    public String getMessage() {
        return message;
    }
}
