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

import static it.polimi.ingsw.utils.Constants.maxNumberOfPlayers;
import static it.polimi.ingsw.utils.Constants.minNumberOfPlayers;

/**
 * A class that represents a Lobby.
 * This class contains the Model and the Controller,
 * and it's required in order to have multiple game instances on the same server.
 */
public class Lobby implements Observer {
    /**
     * The Game Controller of this Lobby.
     */
    private final GameController controller;
    /**
     * The Client Handlers of this Lobby.
     */
    private final Set<ClientHandler> clientHandlers;
    /**
     * A boolean that represents if the lobby can accept new players.
     */
    private boolean isOpen;

    /**
     * Creates a new Lobby with the provided parameters.
     *
     * @param controller the current game controller.
     */
    public Lobby(GameController controller) {
        this.controller = controller;
        this.clientHandlers = new HashSet<>();
    }

    /**
     * @return true if the lobby can accept new players, false otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Adds the provided Client Handler to this Lobby.
     *
     * @param clientHandler the Client Handler to be added.
     */
    public void addClientHandler(ClientHandler clientHandler) {
        this.clientHandlers.add(clientHandler);
    }

    /**
     * Sends the provided message to the controller, which is responsible for initializing a new game.
     *
     * @param numberOfPlayersMessage the number of players the game should have.
     */
    public void update(Integer numberOfPlayersMessage) {
        if (numberOfPlayersMessage < minNumberOfPlayers || numberOfPlayersMessage > maxNumberOfPlayers)
            return;
        this.controller.initGame(numberOfPlayersMessage);
        this.isOpen = true;
    }

    /**
     * Sends the provided message to the clients connected in this lobby,
     * which are responsible for sending it to the client over the network.
     *
     * @param gameViewMessage the message to be sent.
     */
    public void update(GameViewMsg gameViewMessage) {
        if (gameViewMessage.getGameStatus().equals(Game.Status.STARTED))
            this.isOpen = false;


        for (ClientHandler clientHandler : clientHandlers) {
            try {
                ((ClientHandlerImpl) clientHandler).sendMessage(gameViewMessage);
            } catch (RemoteException e) {
                System.err.println("A remote exception was thrown!");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sends the provided message to the controller, which is responsible for adding it to the chat.
     *
     * @param chatMsg the message to be sent.
     */
    public void update(ChatMsg chatMsg) {
        controller.addChatMessage(chatMsg);
    }

    /**
     * Sends the provided message to the controller,
     * which is responsible for actually making the move
     * by inserting the item cards in the player's shelf.
     *
     * @param moveMsg the message to be sent.
     */
    public void update(MoveMsg moveMsg) {
        System.err.println("move made");
        controller.makeMove(moveMsg);
    }

    /**
     * Sends the provided message to the controller,
     * which is responsible for making the current player fill its board.
     *
     * @param cheatMsg the message to be sent.
     */
    public void update(CheatMsg cheatMsg) {
        System.err.println(cheatMsg.getCheater() + " IS CHEATING!");
        controller.cheat();
    }

    /**
     * Sends the specified message to the controller,
     * which is responsible for adding the player in the game.
     *
     * @param playerName the player's name to be added in the game.
     */
    public void update(String playerName) {
        System.err.println("adding player: " + playerName);
        controller.addPlayer(playerName);
    }
}
