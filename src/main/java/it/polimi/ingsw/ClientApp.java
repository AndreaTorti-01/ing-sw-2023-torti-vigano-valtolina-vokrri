package it.polimi.ingsw;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.network.client.ClientImpl;
import it.polimi.ingsw.network.client.RemoteStub;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import static it.polimi.ingsw.utils.Constants.serverIpAddress;

/**
 * Client App main class.
 */
public class ClientApp {
    /**
     * The main method of the client app.
     *
     * @param args the arguments provided from the player.
     */
    public static void main(String[] args) {
        boolean isSocket = false;
        boolean isTui = false;

        // parses the arguments provided from the player
        // in the terminal when launching the client app
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-s") || args[i].equals("--socket")) {
                isSocket = true;
            }
            if (args[i].equals("-cli") || args[i].equals("--terminal")) {
                isTui = true;
            }
            if (args[i].equals("-ip") || args[i].equals("--ip")) {
                serverIpAddress = args[i + 1];
                System.out.println("Server IP address: " + serverIpAddress);
            }
        }

        // generate a client implementation
        ClientImpl client = null;
        try {
            client = new ClientImpl(isTui);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Server server = null;
        if (isSocket) { // Socket
            // connect to virtual server
            server = new RemoteStub();
        } else { // RMI
            // locate the registry (hosted on server machine)
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry(serverIpAddress, 1099);
                // connect to RMI server
                server = (Server) registry.lookup("server");
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }

        ClientHandler clientHandler;
        try {
            assert server != null;
            // register the client object
            clientHandler = server.registerClient(client);

            assert client != null;
            // set the client handler for the client object
            client.setClientHandler(clientHandler);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}