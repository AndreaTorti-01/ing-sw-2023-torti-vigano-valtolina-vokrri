package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.Scanner;

public class Tui extends Observable<String> implements Observer<GameView, String>, Runnable {
    @Override
    public void run() {
        // Ask the names of players
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the number of players: ");
        int nPlayers = scanner.nextInt();
        for (int i = 0; i < nPlayers; i++) {
            System.out.println("Insert the name of player " + i + ": ");
            String name = scanner.next();
            setChanged();
            notifyObservers("name " + name);
        }

        while (true) {
            // Game loop
        }
    }

    @Override
    public void update(GameView o, String arg) {

    }
}
