package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.utils.Observer;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.Tui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Observer, Runnable {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final RunnableView view;

    public Client(Socket socket) {
        this.view = new Tui(this);

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Integer intMsg) {
        try {
            outputStream.writeObject(intMsg);
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

    public void update(String playerName) {
        try {
            outputStream.writeObject(playerName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        GameViewMsg modelView;
        new Thread(view).start();

        do {
            try {
                modelView = (GameViewMsg) inputStream.readObject();
                view.updateView(modelView);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println(e.getMessage());
            }
        } while (true);
    }
}