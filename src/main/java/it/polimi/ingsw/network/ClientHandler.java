package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client Handler Interface.
 */
public interface ClientHandler extends Remote {
    void receiveMessage(Object message) throws RemoteException;
}
