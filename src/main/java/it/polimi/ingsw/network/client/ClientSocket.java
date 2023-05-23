package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.serializable.ChatMsg;
import it.polimi.ingsw.network.serializable.GameViewMsg;
import it.polimi.ingsw.network.serializable.MoveMsg;
import it.polimi.ingsw.view.RunnableView;
import it.polimi.ingsw.view.tui.TuiRaw;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocket implements Client {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final RunnableView view;

    public ClientSocket(Socket socket, boolean isTui) {
        if (isTui) {
            this.view = new TuiRaw(this);
        } else {
            this.view = null; // FIXME
        }


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
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(MoveMsg move) {
        try {
            outputStream.writeObject(move);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(ChatMsg message) {
        try {
            outputStream.writeObject(message);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String playerName) {
        try {
            outputStream.writeObject(playerName);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        GameViewMsg modelView;
        new Thread(view).start();

        //noinspection InfiniteLoopStatement
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