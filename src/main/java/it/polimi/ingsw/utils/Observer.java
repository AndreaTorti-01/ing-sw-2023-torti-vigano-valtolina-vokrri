package it.polimi.ingsw.utils;

public interface Observer<T> {
    default void update(T message) {
        System.err.println("Message type not supported for this class");
    }
}
