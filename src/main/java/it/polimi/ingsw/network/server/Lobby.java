package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.CheatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;

import java.rmi.RemoteException;
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
        if (modelView.getGameStatus().equals(Game.Status.STARTED))
            this.isOpen = false;

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                ((ClientHandlerImpl) clientHandler).sendMessage(modelView);
            } catch (RemoteException e) {
                System.err.println("A remote exception was thrown!");
                throw new RuntimeException(e);
            }
        }
    }

    public void update(ChatMsg chatMsg) {
        controller.addChatMessage(chatMsg);
    }

    public void update(MoveMsg moveMsg) {
        System.err.println("move made");
        controller.makeMove(moveMsg);
    }

    public void update(CheatMsg cheatMsg) {
        System.err.println(cheatMsg.getCheater() + " IS CHEATING!");
        controller.cheat();
    }

    public void update(String playerName) {
        System.err.println("adding player " + playerName);
        controller.addPlayer(playerName);
    }
}
