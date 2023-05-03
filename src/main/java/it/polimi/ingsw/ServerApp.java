package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
    private static Server server;

    public static void main(String[] args) {
        Server server;

        try {
            server = new Server(new ServerSocket(Constants.serverPort));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        server.run();

    }
}
