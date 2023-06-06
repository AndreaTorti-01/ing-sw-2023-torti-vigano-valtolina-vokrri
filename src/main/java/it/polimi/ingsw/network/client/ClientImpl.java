package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.tui.TuiRaw;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Observer {
    @Serial
    private static final long serialVersionUID = 2177950121442992758L;
    private final RunnableView view;
    private ClientHandler clientHandler;


    public ClientImpl(boolean isTui) throws RemoteException {
        super();

        if (isTui) {
            this.view = new TuiRaw(this);
            new Thread(view).start();
        } else {
            this.view = new Gui(this);
            new Thread(view).start();
        }
    }

    public void update(Integer numberOfPlayers) throws RemoteException {
        this.sendMessage(numberOfPlayers);
    }

    public void update(String playerName) throws RemoteException {
        this.sendMessage(playerName);
    }

    public void update(MoveMsg move) throws RemoteException {
        this.sendMessage(move);
    }

    public void update(ChatMsg message) throws RemoteException {
        this.sendMessage(message);
    }

    @Override
    public void receiveMessage(GameViewMsg message) throws RemoteException {
        // update the view with the new modelView
        view.updateView(message);
    }

    public void sendMessage(Object message) {
        try {
            clientHandler.receiveMessage(message);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setClientHandler(ClientHandler clientHandler) throws RemoteException {
        this.clientHandler = clientHandler;
    }
}
