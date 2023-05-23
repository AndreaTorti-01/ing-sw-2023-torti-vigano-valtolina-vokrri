package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.network.server.ClientHandlerRmiInterface;
import it.polimi.ingsw.network.server.ServerRmiInterface;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.tui.TuiRaw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ClientRmi implements ClientRmiInterface {
    private final RunnableView view;
    private String uuid; // FIXME may not be unique
    private Registry registry;
    private ServerRmiInterface server;
    private ClientHandlerRmiInterface clientHandler;

    public ClientRmi(boolean isTui) throws RemoteException {
        super();
        if (isTui) {
            view = new TuiRaw(this);
        } else {
            view = null; // FIXME
        }
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

    public RunnableView getView() {
        return view;
    }

    public void run() {
        this.uuid = UUID.randomUUID().toString();

        try {
            registry = LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        try {
            this.server = (ServerRmiInterface) registry.lookup("server");
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }

        String clientHandlerName;
        try {
            clientHandlerName = server.createClientHandlerAndReturnName();
            clientHandler = (ClientHandlerRmiInterface) registry.lookup(clientHandlerName);
            clientHandler.lookupClient(this.uuid);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
