package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;

public class ClientApp {

    public static void main(String[] args) {
        boolean isSocket = true;

        // parses the arguments provided from the player
        // in the terminal when launching the client app
        for (String arg : args) {
            // if one of the arguments is "-r" or "--rmi"
            // launches the app in rmi mode
            if (arg.equals("-r") || arg.equals("--rmi")) {
                isSocket = false;
                break;
            }
        }

        // if no argument is provided for the rmi launch,
        // it launches the app in socket mode
        if (isSocket) {
            Client client;

            try {
                // requests connection to the server with the specified IP address and port
                client = new Client(new Socket(Constants.serverIpAddress, Constants.serverPort));
            } catch (IOException e) {
                System.err.println("Failed to connect to the server socket");
                throw new RuntimeException(e);
            }

            // runs the client
            client.run();
        }
    }

}
