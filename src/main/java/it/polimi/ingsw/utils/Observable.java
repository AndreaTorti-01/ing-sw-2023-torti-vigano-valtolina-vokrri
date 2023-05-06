package it.polimi.ingsw.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public abstract class Observable {
    private final Vector<Observer> observers;

    public Observable() {
        observers = new Vector<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    public synchronized void deleteObserver(Object o) {
        observers.removeElement(o);
    }

    public void notifyObservers(Object message) {

        for (Observer o : observers) {
            try {
                Method m = o.getClass().getMethod("update", message.getClass());
                m.invoke(o, message);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.err.println(e.getClass());
                System.err.println(e.getMessage());
            }
        }
    }
}
