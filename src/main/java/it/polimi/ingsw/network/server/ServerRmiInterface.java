package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.Server;

import java.rmi.RemoteException;

public interface ServerRmiInterface extends Server {
    String createClientHandlerAndReturnName() throws RemoteException;
}
