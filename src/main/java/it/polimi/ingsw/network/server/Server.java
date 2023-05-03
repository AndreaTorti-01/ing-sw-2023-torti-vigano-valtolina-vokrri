package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private final ServerSocket serverSocket;
    private final List<Lobby> lobbies = new ArrayList<>();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        Socket s;
        boolean freeLobbyFound = false;

        while (true) {
            s = new Socket();
            // accept a new connection
            try {
                System.err.println("accepting client...");
                s = this.serverSocket.accept();
                System.err.println("accepted client...");
            } catch (IOException e) {
                e.printStackTrace();
            }
            // assign the new client to a clientHandler
            ClientHandler clientHandler = new ClientHandler(s);
            // try to find a free lobby
            for (Lobby l : lobbies) {
                if (!l.isGameStarted()) {
                    l.addClientHandler(clientHandler);
                    clientHandler.addObserver(l);
                    freeLobbyFound = true;
                    break;
                }
            }
            // if there's no free lobby, create a new one
            if (!freeLobbyFound) {
                Game model = new Game();
                GameController controller = new GameController(model);
                Lobby l = new Lobby(controller);
                l.addClientHandler(clientHandler);
                lobbies.add(l);
                model.addObserver(l);
            }
        }
    }
}



