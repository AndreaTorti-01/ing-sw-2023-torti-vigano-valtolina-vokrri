package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.CheatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.gui.Gui;
import it.polimi.ingsw.view.tui.Tui;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * A class that implements the Client Interface.
 * This class represents both the RMI and Socket Client.
 */
public class ClientImpl extends UnicastRemoteObject implements Client, Observer {
    @Serial
    private static final long serialVersionUID = 2177950121442992758L;
    /**
     * The View associated with this Client.
     */
    private static RunnableView view;
    /**
     * The Client Handler associated with this Client.
     */
    private ClientHandler clientHandler;

    /**
     * Creates a new client implementation and runs the view
     * (TUI or GUI, based on the provided value).
     *
     * @param isTui true if the user requested the TUI version of the application,
     *              false if requested the GUI version.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public ClientImpl(boolean isTui) throws RemoteException {
        super();

        if (isTui) {
            view = new Tui(this);
//            view = new TuiRaw(this);
            new Thread(view).start();
        } else {
            view = new Gui(this);
            new Thread(view).start();
        }
    }

    /**
     * Gets the view associated with this Client.
     *
     * @return the view selected by the user.
     */
    public static RunnableView getView() {
        return view;
    }

    /**
     * Sends the provided message to the client handler,
     * containing the name of the player requesting to play.
     *
     * @param playerNameMessage the name of the player requesting to play.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void update(String playerNameMessage) throws RemoteException {
        this.sendMessage(playerNameMessage);
    }

    /**
     * Sends the provided message to the client handler,
     * containing the number of players the game should have.
     *
     * @param numberOfPlayersMessage the number of players the game should have.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void update(Integer numberOfPlayersMessage) throws RemoteException {
        this.sendMessage(numberOfPlayersMessage);
    }


    /**
     * Sends the provided message to the client handler,
     * containing the move the player wants to make.
     *
     * @param moveMessage the move that the player wants to make.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void update(MoveMsg moveMessage) throws RemoteException {
        this.sendMessage(moveMessage);
    }

    /**
     * Sends the provided message to the client handler,
     * containing the message the player wants to send to other player
     *
     * @param ChatMessage the message the player wants to send to other players or to a specific one.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void update(ChatMsg ChatMessage) throws RemoteException {
        this.sendMessage(ChatMessage);
    }

    /**
     * Sends the provided message to the client handler,
     * containing the message that makes the current player win.
     *
     * @param cheatMessage the message to be sent, that tells the client handler to make the current player win.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void update(CheatMsg cheatMessage) throws RemoteException {
        this.sendMessage(cheatMessage);
    }

    /**
     * Receives the provided message from the server.
     *
     * @param message the message the server sent to the client.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    @Override
    public void receiveMessage(GameViewMsg message) throws RemoteException {
        // update the view with the new modelView
        view.updateView(message);
    }

    /**
     * Sends the provided message to the client handler.
     *
     * @param message the message to be sent.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    private void sendMessage(Object message) throws RemoteException {
        clientHandler.receiveMessage(message);
    }

    /**
     * Sets the client handler to provided one in order to establish a new connection between client and client handler.
     *
     * @param clientHandler the client handler to connect the client with.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public void setClientHandler(ClientHandler clientHandler) throws RemoteException {
        this.clientHandler = clientHandler;
    }
}
