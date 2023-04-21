package it.polimi.ingsw.utils;

public interface Observer {
    default void update(Object message) {
        System.err.println("Message type not supported for this class");
    }
}
