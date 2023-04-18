package it.polimi.ingsw.utils;

import java.util.Vector;

public abstract class Observable {
    private final Vector<Observer> obs;

    public Observable() {
        obs = new Vector<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!obs.contains(o)) obs.addElement(o);

    }

    public synchronized void deleteObserver(Object o) {
        obs.removeElement(o);
    }

    public void notifyObservers(Object message) {

        for (Observer o : obs) o.update(message);

    }
}
