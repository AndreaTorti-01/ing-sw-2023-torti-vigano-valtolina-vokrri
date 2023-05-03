package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameView;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;

import java.util.HashSet;
import java.util.Set;

public class Lobby implements Observer<ChatMsg> {
    private final GameController controller;
    private final Set<ClientHandler> clientHandlers;
    private boolean isGameStarted;

    public Lobby(GameController controller) {
        this.controller = controller;
        this.clientHandlers = new HashSet<>();
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void addClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.add(clientHandler);
    }

    public void startGame() {
        this.isGameStarted = true;
    }

    public void update(GameView modelView) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMsg(modelView);
        }
    }

    public void update(ChatMsg chatMsg) {
        throw new UnsupportedOperationException();
    }

    public void update(MoveMsg moveMsg) {
        controller.makeMove(moveMsg);
    }
}
