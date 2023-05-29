package it.polimi.ingsw.utils;

public interface Observable {
    void addObserver(Observer o);

    void notifyObservers(Object message);
}
