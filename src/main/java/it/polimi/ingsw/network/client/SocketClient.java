package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.Tui;

public class SocketClient implements Client {
    Tui view = new Tui();

    @Override
    public void disconnect() {

    }

    @Override
    public void update(GameView message) {
        view.update(message);
    }
}
