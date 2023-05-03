package it.polimi.ingsw;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.Tui;

import java.io.IOException;
import java.net.Socket;

public class ClientApp {

    public static void main(String[] args) {
        Client client;

        try {
            client = new Client(new Socket(Constants.serverIpAddress, Constants.serverPort));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        client.run();
    }

}
