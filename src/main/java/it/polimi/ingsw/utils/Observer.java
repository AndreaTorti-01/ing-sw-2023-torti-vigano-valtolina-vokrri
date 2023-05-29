package it.polimi.ingsw.utils;

/**
 * Observer interface implemented by classes which wants to be informed of changes of an Observable object
 */
public interface Observer {
    default void update(Object message) {
        System.err.println("Message type not supported for this class " + message.getClass().getSimpleName());
    }
}
