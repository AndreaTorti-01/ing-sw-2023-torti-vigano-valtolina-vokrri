package it.polimi.ingsw.client;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.Tui;

public class App {
    public static void main(String[] args) {

        Game model = new Game();
        Tui view = new Tui();
        GameController controller = new GameController(model, view);

        view.run();

    }
}
