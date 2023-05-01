package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
    private static Server server;

    public static void main(String[] args) {
        /* QUESTO CODICE SERVIRA PIÙ AVANTI
         *  int serverPort = 42069;
         *
         *  for (int i = 0; i < args.length; i++) {
         *      if (args.length >= 2 && args[i].equals("--port")) {
         *          try {
         *              serverPort = Integer.parseInt(args[i + 1]);
         *          } catch (IllegalArgumentException e) {
         *              System.err.println("Invalid port. Using default one!");
         *          }
         *      }
         *  }
         */

        server = createServer();
        server.start();

    }

    private static Server createServer() {
        try {
            return new Server(new ServerSocket(Constants.serverPort));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
