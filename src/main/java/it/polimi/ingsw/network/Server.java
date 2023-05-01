package it.polimi.ingsw.network;

import it.polimi.ingsw.network.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private List<Lobby> lobbies;
    private ServerSocket serverSocket;

    public void server() {
        try {
            this.serverSocket = new ServerSocket(8888);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        Socket s;
        while (true) {
            s = new Socket();
            try {

                s = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean freeLobbyFound = false;

        ClientHandler ch = new ClientHandler(s);

        for (Lobby l : lobbies) {
            if (l.isGameStarted() == false) {
                l.clientHandlers.add(ch);
                freeLobbyFound = true;
                break;
            }

        }
        if (!freeLobbyFound) {
            Lobby l = new Lobby();
            l.clientHandlers.add(ch);
            lobbies.add(l);
        }
    }

}



