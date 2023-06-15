package it.polimi.ingsw.network;

import it.polimi.ingsw.network.serializable.GameViewMsg;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Client Interface.
 */
public interface Client extends Remote {
    void receiveMessage(GameViewMsg message) throws RemoteException;
}
