package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServerSocket implements Server, Runnable {
    private final java.net.ServerSocket serverSocket;
    private final List<Lobby> lobbies = new LinkedList<>();

    public ServerSocket(java.net.ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        Socket socket;
        boolean freeLobbyFound = false;

        //noinspection InfiniteLoopStatement
        while (true) {
            socket = new Socket();
            // accept a new connection
            try {
                System.err.println("accepting client...");
                socket = this.serverSocket.accept();
                System.err.println("accepted client...");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // assign the new client to a clientHandler
            ClientHandlerSocket clientHandler = new ClientHandlerSocket(socket);
            // try to find a free lobby
            for (Lobby lobby : lobbies) {
                if (lobby.isOpen()) {
                    lobby.addClientHandler(clientHandler);
                    clientHandler.addObserver(lobby);

                    freeLobbyFound = true;
                    new Thread(clientHandler).start();
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

                new Thread(clientHandler).start();
            }
        }
    }
}



