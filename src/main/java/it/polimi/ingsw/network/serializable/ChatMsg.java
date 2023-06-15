package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class that represents a Chat Message.
 */
public class ChatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String destinationPlayer;
    private final String sourcePlayer;
    private final boolean isPublic;
    private final String message;

    /**
     * Creates a new Chat Message with the provided parameters.
     *
     * @param destinationPlayer the player to whom the message is intended.
     * @param sourcePlayer      the player who sent the message.
     * @param isPublic          whether the message is public or not.
     * @param message           the message to be sent.
     */
    public ChatMsg(String destinationPlayer, String sourcePlayer, boolean isPublic, String message) {
        this.destinationPlayer = destinationPlayer;
        this.sourcePlayer = sourcePlayer;
        this.isPublic = isPublic;
        this.message = message;
    }

    /**
     * @return the player to whom the message is intended.
     */
    public String getDestinationPlayer() {
        return destinationPlayer;
    }

    /**
     * @return The player who sent the message.
     */
    public String getSourcePlayer() {
        return sourcePlayer;
    }

    /**
     * @return true if the message is public, false otherwise.
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * @return the message to be sent.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return a string representation of this message.
     */
    @Override
    public String toString() {
        String msgType = isPublic ? "" : "(private) ";
        return msgType + sourcePlayer + ": " + message;
    }
}
