package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utils.Constants;

import java.io.IOException;
import java.net.Socket;

public class ClientApp {

    private static Client client;

    public static void main(String[] args) {
        /* QUESTO CODICE SERVIRA PIÙ AVANTI
         *  boolean isTerminalApp = false;
         *
         *
         *  // checks if the user requested the TUI app
         *  for (String arg : args) {
         *      if (arg.equals("--cli")) {
         *          isTerminalApp = true;
         *          break;
         *      }
         *  }
         *
         *  if (isTerminalApp) {
         *      // launches the Textual User Interface
         *      Tui view = new Tui();
         *
         *  } else {
         *      // launches the Graphical User Interface
         *      Gui gui = new Gui();
         *
         *  }
         */

        client = createClient();
        client.start();

    }

    private static Client createClient() {
        try {
            return new Client(
                    new Socket(Constants.serverIpAddress, Constants.serverPort)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
