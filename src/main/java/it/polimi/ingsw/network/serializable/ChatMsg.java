package it.polimi.ingsw.network.serializable;

import java.io.Serial;
import java.io.Serializable;

/**
 * A class that represents a Chat Message.
 */
public class ChatMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * The player to whom the message is intended.
     */
    private final String recipientPlayer;
    /**
     * The player who sent the message.
     */
    private final String senderPlayer;
    /**
     * True if the message is public.
     */
    private final boolean isPublic;
    /**
     * The message to be sent.
     */
    private final String message;

    /**
     * Creates a new Chat Message with the provided parameters.
     *
     * @param recipientPlayer the player to whom the message is intended.
     * @param senderPlayer    the player who sent the message.
     * @param isPublic        whether the message is public or not.
     * @param message         the message to be sent.
     */
    public ChatMsg(String recipientPlayer, String senderPlayer, boolean isPublic, String message) {
        this.recipientPlayer = recipientPlayer;
        this.senderPlayer = senderPlayer;
        this.isPublic = isPublic;
        this.message = message;
    }

    /**
     * Gets the player to whom the message is intended.
     *
     * @return the player to whom the message is intended.
     */
    public String getRecipientPlayer() {
        return recipientPlayer;
    }

    /**
     * Gets the player who sent the message.
     *
     * @return The player who sent the message.
     */
    public String getSenderPlayer() {
        return senderPlayer;
    }

    /**
     * Tells whether the message is public or not.
     *
     * @return true if the message is public, false otherwise.
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Gets the message to be sent.
     *
     * @return the message to be sent.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets a string representation of this message.
     *
     * @return a string representation of this message.
     */
    @Override
    public String toString() {
        String msgType = isPublic ? "" : "(private) ";
        return msgType + senderPlayer + ": " + message;
    }
}
