package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server, Runnable {
    @Serial
    private static final long serialVersionUID = -6751882795824411L;
    private final List<Lobby> lobbies = new LinkedList<>();

    public ServerImpl() throws RemoteException {
        super();
    }

    @Override
    public ClientHandler registerClient(Client client) throws RemoteException {
        ClientHandlerImpl clientHandler = new ClientHandlerImpl(client);

        boolean freeLobbyFound = false;

        // try to find a free lobby
        for (Lobby lobby : lobbies) {
            if (lobby.isOpen()) {
                lobby.addClientHandler(clientHandler);
                clientHandler.addObserver(lobby);

                freeLobbyFound = true;
                break;
            }
        }

        // if there's no free lobby, create a new one
        if (!freeLobbyFound) {
            Game model = new Game();
            GameController controller = new GameController(model);
            Lobby lobby = new Lobby(controller);
            lobbies.add(lobby);
            model.addObserver(lobby);

            lobby.addClientHandler(clientHandler);
            clientHandler.addObserver(lobby);
        }

        return clientHandler;
    }

    @Override
    public void run() {
        Socket socket = null;
        ClientHandler clientHandler = null;
        ClientSkeleton clientSkeleton = null;

        //noinspection InfiniteLoopStatement
        while (true) {
            // accept connection from serverStub
            try (ServerSocket serverSocket = new ServerSocket(8888)) {
                socket = serverSocket.accept();

                System.err.println(
                        "ServerImpl: accettata connessione da " + socket.getInetAddress() + ":" + socket.getPort());

            } catch (IOException e) {
                e.printStackTrace();
            }

            // create a clientSkeleton for the newly connected client
            assert socket != null;
            clientSkeleton = new ClientSkeleton(socket);

            // creates a clientHandler for the newly created clientSkeleton
            try {
                clientHandler = this.registerClient(clientSkeleton);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            clientSkeleton.setClientHandler(clientHandler);

            // enables the server to listen to clients
            new Thread(clientSkeleton).start();
        }
    }
}
