package it.polimi.ingsw.utils;

/**
 * An interface to be implemented by classes that want to notify changes of their state to Observer classes.
 */
public interface Observable {
    /**
     * Adds the provided observer to the list of observers.
     *
     * @param o the observer to be added.
     */
    void addObserver(Observer o);

    /**
     * Notifies with the provided message the observers in the list of observers.
     *
     * @param message the message with which to notify the observers.
     */
    void notifyObservers(Object message);
}