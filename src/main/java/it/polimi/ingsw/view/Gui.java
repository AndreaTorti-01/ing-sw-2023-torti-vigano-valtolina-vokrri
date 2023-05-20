package it.polimi.ingsw.view;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;

public class Gui extends Application {
    private final JFrame frame;
    private final JPanel panel;
    private final JButton button;
    private final JLabel label;

    public Gui() {
        frame = new JFrame("Frame");
        panel = new JPanel();
        button = new JButton("Button");
        label = new JLabel("Label");

        panel.add(button);
        panel.add(label);
        frame.add(panel);
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    @Override
    public void start(Stage stage) throws Exception {
        throw new UnsupportedOperationException();
    }
}
