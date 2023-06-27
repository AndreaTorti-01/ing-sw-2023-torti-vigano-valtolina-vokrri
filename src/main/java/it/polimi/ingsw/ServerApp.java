package it.polimi.ingsw;

import it.polimi.ingsw.network.server.ServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server App main class.
 */
public class ServerApp {

    /**
     * The main method of the server app.
     *
     * @param args the arguments provided from the player.
     */
    public static void main(String[] args) {
        try {
            // generate a server implementation
            ServerImpl server = new ServerImpl();
            // create a registry
            Registry registry = LocateRegistry.createRegistry(1099);

            // bind the server object to the registry
            registry.rebind("server", server);

            // start the server
            new Thread(server).start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        System.err.println("Server started");
    }
}
