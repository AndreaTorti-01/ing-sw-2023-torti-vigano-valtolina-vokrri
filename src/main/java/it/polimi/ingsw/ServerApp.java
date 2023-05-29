package it.polimi.ingsw;

import it.polimi.ingsw.network.server.ServerImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {

    public static void main(String[] args) {
        // // default port 8888
        // int serverPort = Constants.serverPort;
        // // parses the port at which the server should listen
        // // for connection requests from the clients
        // for (int i = 0; i < args.length - 1; i++) {
        //     // if one of the arguments provided is "-p" or "--port",
        //     if (args[i].equals("--port") || args[i].equals("-p")) {
        //         try {
        //             serverPort = Integer.parseInt(args[i + 1]);
        //         } catch (NumberFormatException | NullPointerException e) {
        //             System.err.println("Failed to parse the port at which the server should listen.\n Connecting to the default one");
        //         }
        //     }
        // }


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
