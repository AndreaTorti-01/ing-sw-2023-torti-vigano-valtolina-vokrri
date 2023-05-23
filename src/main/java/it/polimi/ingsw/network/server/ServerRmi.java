package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;

import java.io.Serial;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class ServerRmi extends UnicastRemoteObject implements ServerRmiInterface {
    @Serial
    private static final long serialVersionUID = -5433204935316346424L;
    private final List<Lobby> lobbies = new LinkedList<>();
    private final Registry registry;

    public ServerRmi(Registry registry) throws RemoteException {
        super();
        this.registry = registry;
    }

    public void run() throws RemoteException {
        registry.rebind("server", (ServerRmiInterface) this);
        System.err.println("Rmi Server started");
    }

    @Override
    public String createClientHandlerAndReturnName() throws RemoteException {
        ClientHandlerRmi clientHandler = new ClientHandlerRmi(registry);

        for (Lobby lobby : lobbies) {
            if (lobby.isOpen()) {
                lobby.addClientHandler(clientHandler);
                clientHandler.addObserver(lobby);

                clientHandler.addToRegistry();

                return clientHandler.getUuid();
            }
        }

        // if no free lobby found create one and add clientHandler
        Game model = new Game();
        GameController controller = new GameController(model);
        Lobby lobby = new Lobby(controller);
        lobbies.add(lobby);
        model.addObserver(lobby);

        lobby.addClientHandler(clientHandler);
        clientHandler.addObserver(lobby);

        clientHandler.addToRegistry();

        return clientHandler.getUuid();
    }
}
