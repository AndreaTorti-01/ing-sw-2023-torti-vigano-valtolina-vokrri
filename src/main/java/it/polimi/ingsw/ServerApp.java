package it.polimi.ingsw;

import it.polimi.ingsw.network.server.ServerRmi;
import it.polimi.ingsw.network.server.ServerSocket;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerApp {

    public static void main(String[] args) {
        // default port 8888
        int serverPort = Constants.serverPort;

        boolean isSocket = false;
        for (String arg : args) {
            // socket only mode
            if (arg.equals("-s") || arg.equals("--socket")) {
                isSocket = true;
                break;
            }
        }

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
        initSocketServer(serverPort);

        // if there is no indication to run socket only, runs rmi server too
        if (!isSocket) {
            initRmiServer();
        }

    }

    static private void initSocketServer(int serverPort) {
        ServerSocket serverSocket;
        try {
            // creates a new serverSocket socket for the client to connect
            // at the specified port if provided, or at the default one
            serverSocket = new ServerSocket(new java.net.ServerSocket(serverPort));
        } catch (IOException e) {
            System.err.println("Failed to create the socket server");
            throw new RuntimeException(e);
        }

        // runs the newly created socket server
        new Thread(serverSocket).start();
    }

    static private void initRmiServer() {
        final Registry registry;
        try {
            // creates a new RMI registry at the default 1099 port
            registry = LocateRegistry.createRegistry(Constants.rmiServerPort);
            // creates a new RMIServer...
            ServerRmi rmiServer = new ServerRmi(registry);
            //...and runs it
            rmiServer.run();
        } catch (RemoteException e) {
            System.err.println("Failed to create the RMI server");
            throw new RuntimeException(e);
        }
    }
}
