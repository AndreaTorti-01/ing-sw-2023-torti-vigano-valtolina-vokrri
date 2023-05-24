package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.network.server.ClientHandlerRmiInterface;
import it.polimi.ingsw.network.server.ServerRmiInterface;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.tui.TuiRaw;

import java.io.Serial;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class ClientRmi extends UnicastRemoteObject implements ClientRmiInterface {
    @Serial
    private static final long serialVersionUID = 4369082279461316690L;
    private final RunnableView view;
    private String uuid; // FIXME may not be unique
    private Registry registry;
    private ServerRmiInterface server;
    private ClientHandlerRmiInterface clientHandler;

    public ClientRmi(boolean isTui) throws RemoteException {
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

    public void run() throws RemoteException {
        this.uuid = UUID.randomUUID().toString();

        try {
            registry = LocateRegistry.getRegistry();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        registry.rebind("client#" + uuid, this);


        try {
            this.server = (ServerRmiInterface) registry.lookup("server");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

        String clientHandlerName = null;
        clientHandlerName = "clientHandler#" + server.createClientHandlerAndReturnName();
        try {
            clientHandler = (ClientHandlerRmiInterface) registry.lookup(clientHandlerName);
            clientHandler.lookupClient("client#" + this.uuid);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
    }
}
