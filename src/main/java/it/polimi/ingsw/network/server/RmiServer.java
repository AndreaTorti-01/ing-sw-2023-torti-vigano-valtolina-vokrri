package it.polimi.ingsw.network.server;


import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public class RmiServer implements Server, Remote, Runnable {

    @Override
    public void addClient(Client client) throws RemoteException {

    }

    @Override
    public void removeClient(Client client) throws RemoteException {

    }

    @Override
    public void onClientDisconnection(Client client) throws RemoteException {

    }

    @Override
    public void forwardMessage(Object message) throws RemoteException {

    }

    @Override
    public void run() {

    }
}
