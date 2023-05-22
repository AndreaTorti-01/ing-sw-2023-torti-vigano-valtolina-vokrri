package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.network.server.RmiClientHandler;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.Tui;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiClient implements Client {
    private final RunnableView view;
    private Registry registry;
    private RmiClientHandler clientHandler;

    public RmiClient() throws RemoteException {
        super();
        try {
            registry = LocateRegistry.getRegistry();
            this.clientHandler = (RmiClientHandler) registry.lookup("clientHandler");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        view = new Tui(this);
    }

    @Override
    public void update(Integer numberOfPlayers) throws RemoteException {
        clientHandler.notifyObservers(numberOfPlayers);
    }

    @Override
    public void update(String playerName) throws RemoteException {
        clientHandler.notifyObservers(playerName);
    }

    @Override
    public void update(MoveMsg move) throws RemoteException {
        clientHandler.notifyObservers(move);

    }

    @Override
    public void update(ChatMsg message) throws RemoteException {
        clientHandler.notifyObservers(message);
    }

    public void run() {
        GameViewMsg modelView;
        new Thread(view).start();


        //noinspection InfiniteLoopStatement
        do {
            
        } while (true);
    }
}
