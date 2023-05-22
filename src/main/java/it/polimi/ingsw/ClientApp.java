package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.RmiClient;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientApp {

    public static void main(String[] args) {
        boolean isSocket = false;
        Client client;

        // parses the arguments provided from the player
        // in the terminal when launching the client app
        for (String arg : args) {
            // if one of the arguments is "-r" or "--rmi"
            // launches the app in rmi mode
            if (arg.equals("-s") || arg.equals("--socket")) {
                isSocket = true;
                break;
            }
        }

        // if no argument is provided for the rmi launch,
        // it launches the app in socket mode
        if (isSocket) {
            try {
                // requests connection to the server with the specified IP address and port
                client = new SocketClient(new Socket(Constants.serverIpAddress, Constants.serverPort));
            } catch (IOException e) {
                System.err.println("Failed to connect to the server socket");
                throw new RuntimeException(e);
            }

        } else {
            try {
                Registry registry = LocateRegistry.getRegistry();
                client = (RmiClient) registry.lookup("server");

            } catch (RemoteException | NotBoundException e) {
                throw new RuntimeException(e);
            }
        }

        // runs the client
        try {
            client.run();
        } catch (RemoteException e) {
            System.err.println("Some exception occurred while connecting running...");
            throw new RuntimeException(e);
        }

    }

}
