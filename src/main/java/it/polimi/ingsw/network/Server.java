package it.polimi.ingsw.network;

import it.polimi.ingsw.utils.Observer;

import java.rmi.RemoteException;

public interface Server extends Observer {
    /**
     * Adds the client to the server.
     *
     * @param client the client to register.
     * @throws RemoteException in case the communication over the network fails.
     */
    void addClient(Client client) throws RemoteException;

    /**
     * Removes the specified client.
     *
     * @param client the client to remove.
     * @throws RemoteException in case the communication over the network fails.
     */
    void removeClient(Client client) throws RemoteException;

    /**
     * Handles the disconnection of a client.
     *
     * @param client the client that disconnected.
     * @throws RemoteException in case the communication over the network fails.
     */
    void onClientDisconnection(Client client) throws RemoteException;

    /**
     * Handles the received message from the client and forwards it to the GameController.
     *
     * @param message the information to forward.
     * @throws RemoteException in case the communication over the network fails.
     */
    void forwardMessage(Object message) throws RemoteException;

    /**
     * Notifies the server tha a client has interacted.
     *
     * @param client the client that generated the interaction.
     * @throws RemoteException in case the communication over the network fails.
     */
    default void update(Client client) throws RemoteException {
    }
}
