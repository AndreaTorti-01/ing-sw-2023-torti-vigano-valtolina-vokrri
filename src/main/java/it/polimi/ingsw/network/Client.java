package it.polimi.ingsw.network;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.utils.Observer;

public interface Client extends Observer {
    /**
     * Disconnects the current client from the server
     */
    void disconnect();

    /**
     * Notifies the client of a change in the model
     *
     * @param modelView the model view
     */
    void update(GameView modelView);
}
