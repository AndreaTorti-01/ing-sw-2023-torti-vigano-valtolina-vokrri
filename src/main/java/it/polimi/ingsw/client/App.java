package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.Tui;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {

        // ask for number of players and player names
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many players?");
        int numberOfPlayers = scanner.nextInt();
        String[] playerNames = new String[numberOfPlayers];
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Player " + (i + 1) + " name?");
            playerNames[i] = scanner.next();
        }

        // instantiate model, view and controller
        Game model = new Game(playerNames);

        GameController controller = new GameController();

        Tui view = new Tui();

    }
}
