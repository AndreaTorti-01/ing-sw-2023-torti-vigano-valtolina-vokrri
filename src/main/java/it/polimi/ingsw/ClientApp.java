package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.client.ClientRmi;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientApp {

    public static void main(String[] args) {
        boolean isSocket = false;
        boolean isTui = false;
        Client client;

        // parses the arguments provided from the player
        // in the terminal when launching the client app
        for (String arg : args) {
            // if one of the arguments is "-s" or "--socket"
            // launches the app in socket mode
            if (arg.equals("-s") || arg.equals("--socket")) {
                isSocket = true;
            }

            if (arg.equals("-cli") || arg.equals("--terminal")) {
                isTui = true;
            }
        }

        // if no argument is provided for the rmi launch,
        // it launches the app in socket mode
        if (isSocket) {
            try {
                // requests connection to the server with the specified IP address and port
                client = new ClientSocket(new Socket(Constants.serverIpAddress, Constants.serverPort), isTui);
            } catch (IOException e) {
                System.err.println("Failed to connect to the server socket");
                throw new RuntimeException(e);
            }

        } else {
            try {
                Registry registry = LocateRegistry.getRegistry();
                client = new ClientRmi(isTui);

            } catch (RemoteException e) {
                System.err.println("Failed to create the client RMI");
                throw new RuntimeException(e);
            }
        }

        // runs the client
        client.run();
    }
}