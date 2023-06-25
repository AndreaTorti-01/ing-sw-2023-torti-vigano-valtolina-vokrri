package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client Interface.
 */
public interface Client extends Remote {
    /**
     * Receives the provided message.
     *
     * @param message the message to be received.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    void receiveMessage(GameViewMsg message) throws RemoteException;
}
