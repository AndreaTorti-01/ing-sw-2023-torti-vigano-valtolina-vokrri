package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client Handler Interface.
 */
public interface ClientHandler extends Remote {
    /**
     * Receives the provided message.
     *
     * @param message the message to be received.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    void receiveMessage(Object message) throws RemoteException;
}
