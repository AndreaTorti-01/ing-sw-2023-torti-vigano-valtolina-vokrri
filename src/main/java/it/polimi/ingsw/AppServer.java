package it.polimi.ingsw;

import it.polimi.ingsw.network.Server;

public class AppServer {
    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
