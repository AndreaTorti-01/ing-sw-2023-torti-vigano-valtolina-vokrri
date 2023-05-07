package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {

    public static void main(String[] args) {
        int serverPort = Constants.serverPort;

        // parses the port at which the server should listen
        // for connection requests from the clients
        for (int i = 0; i < args.length - 1; i++) {
            // if one of the arguments provided is "-p" or "--port",
            if (args[i].equals("--port") || args[i].equals("-p")) {
                try {
                    serverPort = Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException | NullPointerException e) {
                    System.err.println("Failed to parse the port at which the server should listen.\n Connecting to the default one");
                }
            }
        }

        Server server;
        try {
            // creates a new server socket for the client to connect
            // at the specified port if provided, or at the default one
            server = new Server(new ServerSocket(serverPort));
        } catch (IOException e) {
            System.err.println("Failed to create the server socket");
            throw new RuntimeException(e);
        }

        server.run();
    }
}
