package it.polimi.ingsw.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server Interface.
 */
public interface Server extends Remote {
    /**
     * Registers the provided Client to the server.
     *
     * @param client the Client to be registered.
     * @return the Client Handler associated with the registered Client.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    ClientHandler registerClient(Client client) throws RemoteException;

}
