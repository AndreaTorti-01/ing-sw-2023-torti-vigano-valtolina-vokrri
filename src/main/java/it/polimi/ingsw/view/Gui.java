package it.polimi.ingsw.view;

import javax.swing.*;

public class Gui {
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
}
