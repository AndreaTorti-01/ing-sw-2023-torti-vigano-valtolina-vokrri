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
    public static void main(String[] args) {
        boolean isSocket = false;
        boolean isTui = false;

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
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(serverIpAddress, 1099);
                // connect to RMI server
                server = (Server) registry.lookup("server");
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        }

        ClientHandler clientHandler = null;
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