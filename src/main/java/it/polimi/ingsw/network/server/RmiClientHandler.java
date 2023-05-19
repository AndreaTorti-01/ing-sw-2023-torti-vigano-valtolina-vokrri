package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.utils.Observable;

import java.rmi.RemoteException;

public class RmiClientHandler extends Observable implements ClientHandler {
    private final RmiClient client;

    public RmiClientHandler(RmiClient client) throws RemoteException {

        this.client = client;
    }

    @Override
    public void sendMsg(Object msg) {

    }

    @Override
    public void run() {
        
    }
}
