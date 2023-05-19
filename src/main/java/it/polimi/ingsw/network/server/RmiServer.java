package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.client.RmiClient;

import java.io.Serial;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class RmiServer extends UnicastRemoteObject implements Server {
    @Serial
    private static final long serialVersionUID = -5433204935316346424L;
    private final List<Lobby> lobbies = new LinkedList<>();
    private final Registry registry;

    public RmiServer(Registry registry) throws RemoteException {
        super();
        this.registry = registry;
        registry.rebind("server", this);
    }

    @Override
    public void run() {
        RmiClient client;
        try {
            client = (RmiClient) registry.lookup("client");
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

        boolean freeLobbyFound = false;

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                RmiClientHandler clientHandler = new RmiClientHandler(client);

                for (Lobby lobby : lobbies) {
                    if (lobby.isOpen()) {
                        lobby.addClientHandler(clientHandler);
                        clientHandler.addObserver(lobby);

                        freeLobbyFound = true;
                        new Thread(clientHandler).start();
                        break;
                    }
                }

                if (!freeLobbyFound) {
                    Game model = new Game();
                    GameController controller = new GameController(model);
                    Lobby lobby = new Lobby(controller);
                    lobbies.add(lobby);
                    model.addObserver(lobby);

                    lobby.addClientHandler(clientHandler);
                    clientHandler.addObserver(lobby);

                    new Thread(clientHandler).start();
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
