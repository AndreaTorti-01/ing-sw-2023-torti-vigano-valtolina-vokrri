package it.polimi.ingsw.utils;

/**
 * An interface to be implemented by classes that wants to be notified of changes in Observable objects.
 */
public interface Observer {
    /**
     * Notifies the Observer object of the changes contained in the provided message.
     *
     * @param message the message containing information about the changes.
     */
    default void update(Object message) {
        System.err.println("Message type not supported for this class " + message.getClass().getSimpleName());
    }
}