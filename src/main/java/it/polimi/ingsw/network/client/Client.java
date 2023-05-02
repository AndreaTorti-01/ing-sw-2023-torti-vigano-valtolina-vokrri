package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameView;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.Tui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread implements Observer {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final RunnableView view;

    public Client(Socket socket) {
        this.view = new Tui();

        try {
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(MoveMsg move) {
        try {
            outputStream.writeObject(move);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(ChatMsg message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String message) {
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                GameView modelView = (GameView) inputStream.readObject();
                view.updateView(modelView);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
