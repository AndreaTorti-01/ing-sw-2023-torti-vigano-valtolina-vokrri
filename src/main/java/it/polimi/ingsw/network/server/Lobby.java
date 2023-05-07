package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;

import java.util.HashSet;
import java.util.Set;

public class Lobby implements Observer {
    private final GameController controller;
    private final Set<ClientHandler> clientHandlers;
    private boolean isOpen;

    public Lobby(GameController controller) {
        this.controller = controller;
        this.clientHandlers = new HashSet<>();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void addClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.add(clientHandler);
    }

    public void update(Integer intMsg) {
        this.controller.initGame(intMsg);
        this.isOpen = true;
    }

    public void update(GameViewMsg modelView) {
        if (modelView.getGameStatus().equals(GameStatus.STARTED)) {
            this.isOpen = false;
        }

        for (ClientHandler clientHandler : clientHandlers) {
            System.err.println("sending modelView to client" + clientHandler.toString());
            clientHandler.sendMsg(modelView);
        }
    }

    public void update(ChatMsg chatMsg) {
        throw new UnsupportedOperationException();
    }

    public void update(MoveMsg moveMsg) {
        controller.makeMove(moveMsg);
    }

    public void update(String playerName) {
        System.err.println("adding player " + playerName);
        controller.addPlayer(playerName);
    }
}
