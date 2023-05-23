package it.polimi.ingsw.view.gui;

import javafx.stage.Stage;

public class GuiTestRunner {
    public static void main(String[] args) throws Exception {
        Gui gui = new Gui();

        Stage stage = new Stage(null);
        //call the start method of the Gui class
        gui.start(stage);

    }
}
