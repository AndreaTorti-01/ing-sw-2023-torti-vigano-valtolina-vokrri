package it.polimi.ingsw.view;

import it.polimi.ingsw.model.GameView;
import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

public class Tui extends Observable<String> implements Observer<GameView, String>, Runnable {
    @Override
    public void run() {
        // Ask the names of players

        while (true) {
            // Game loop
        }
    }

    @Override
    public void update(GameView o, String arg) {

    }
}
