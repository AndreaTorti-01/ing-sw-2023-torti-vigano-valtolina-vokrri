package it.polimi.ingsw;

import it.polimi.ingsw.view.Gui;
import it.polimi.ingsw.view.Tui;

public class ClientApp {
    public static void main(String[] args) {
        boolean isTerminalApp = false;

        // checks if the user requested the TUI app
        for (String arg : args) {
            if (arg.equals("--cli")) {
                isTerminalApp = true;
                break;
            }
        }

        if (isTerminalApp) {
            // launches the Textual User Interface
            Tui view = new Tui();

        } else {
            // launches the Graphical User Interface
            Gui gui = new Gui();

        }
    }
}
