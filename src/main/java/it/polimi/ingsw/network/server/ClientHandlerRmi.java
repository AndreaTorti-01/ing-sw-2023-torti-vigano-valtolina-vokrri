package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.client.ClientRmiInterface;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.utils.Observable;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ClientHandlerRmi extends Observable implements ClientHandlerRmiInterface, Remote {
    private final String uuid; // FIXME potrebbe coincidere con uno remoto...
    private final Registry registry;
    private ClientRmiInterface client;


    public ClientHandlerRmi(Registry registry) throws RemoteException {
        this.uuid = UUID.randomUUID().toString();
        this.registry = registry;
    }

    @Override
    public void sendMsg(Object msg) {
        client.getView().updateView((GameViewMsg) msg);
    }

    public String getUuid() {
        return uuid;
    }

    public void addToRegistry() {
        try {
            registry.rebind("clientHandler#" + uuid, (ClientHandlerRmiInterface) this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * to be called by client
     *
     * @param clientName
     */
    public void lookupClient(String clientName) {
        try {
            this.client = (ClientRmiInterface) registry.lookup(clientName);
        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
