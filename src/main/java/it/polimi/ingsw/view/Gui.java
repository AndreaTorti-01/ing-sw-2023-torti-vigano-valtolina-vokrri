package it.polimi.ingsw.view;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Gui {
    private JFrame frame;
    private JPanel panel;
    private JButton button;
    private JLabel label;

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
