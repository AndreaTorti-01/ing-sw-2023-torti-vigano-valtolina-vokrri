package it.polimi.ingsw.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public abstract class ObservableImpl {
    private final Vector<Observer> observers;

    public ObservableImpl() {
        observers = new Vector<>();
    }

    public synchronized void addObserver(Observer o) {
        if (o == null) throw new NullPointerException();
        if (!observers.contains(o)) {
            observers.addElement(o);
        }
    }

    public void notifyObservers(Object message) {

        for (Observer o : observers) {
            try {
                Method m = o.getClass().getMethod("update", message.getClass());
                m.invoke(o, message);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                System.err.println(message.getClass().getSimpleName());
                System.err.println(o.getClass().getSimpleName());
                System.err.println(e.getClass());
                System.err.println(e.getMessage());
            }
        }
    }
}
