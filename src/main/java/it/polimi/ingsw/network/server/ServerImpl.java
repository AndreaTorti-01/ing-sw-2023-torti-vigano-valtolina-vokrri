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

/**
 * A class that implements the Server Interface.
 * This class has the only task to accept new client connection and create new lobbies if needed.
 */
public class ServerImpl extends UnicastRemoteObject implements Server, Runnable {
    @Serial
    private static final long serialVersionUID = -6751882795824411L;
    /**
     * The list of lobbies currently active.
     */
    private final List<Lobby> lobbies = new LinkedList<>();

    /**
     * Creates a new Server Implementation.
     *
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
    public ServerImpl() throws RemoteException {
        super();
    }

    /**
     * Registers the provided Client and adds it in a free lobby, if found.
     * If no lobbies are available, creates a new one and adds the client to it.
     * <p>
     * Furthermore, creates a new Client Handler to be associated with the provided Client.
     *
     * @param client the client to be registered.
     * @return the Client Handler associated with the provided Client.
     * @throws RemoteException if something goes wrong with the RMI connection.
     */
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

    /**
     * Waits for new Client socket connection requests.
     */
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
                        "ServerImpl: connection accepted from " + socket.getInetAddress() + ":" + socket.getPort());

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
