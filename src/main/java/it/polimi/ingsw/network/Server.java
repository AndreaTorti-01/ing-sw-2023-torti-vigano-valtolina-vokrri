package it.polimi.ingsw.network;

import java.rmi.Remote;

public interface Server extends Remote, Runnable {
    void run();
}
