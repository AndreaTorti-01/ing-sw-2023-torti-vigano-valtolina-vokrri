package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.ClientHandler;

public interface ClientHandlerRmiInterface extends ClientHandler {
    void notifyObservers(Object msg);

    void lookupClient(String uuid);
}
