package it.polimi.ingsw.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

/**
 * An abstract class that implements the Observable interface.
 */
public abstract class ObservableImpl {
    /**
     * The vector of observers.
     */
    private final Vector<Observer> observers;

    /**
     * Creates a new Observable Implementation.
     */
    public ObservableImpl() {
        observers = new Vector<>();
    }

    /**
     * Adds the provided observer to the list of observers.
     *
     * @param o the observer to be added.
     */
    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    /**
     * Notifies with the provided message the observers in the list of observers.
     *
     * @param message the message with which to notify the observers.
     */
    public void notifyObservers(Object message) {

        for (Observer o : observers) {
            try {
                Method m = o.getClass().getMethod("update", message.getClass());
                m.invoke(o, message);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
