package it.polimi.ingsw.utils;

import java.util.Vector;

public abstract class Observable {
    private final Vector<Observer> observers;

    public Observable() {
        observers = new Vector<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null)
            throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    public synchronized void deleteObserver(Object o) {
        observers.removeElement(o);
    }

    public void notifyObservers(Object message) {
        for (Observer o : observers) o.update(message);
    }
}
