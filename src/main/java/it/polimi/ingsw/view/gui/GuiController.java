package it.polimi.ingsw.view.gui;

import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class GuiController {
    public ImageView nickname_label;
    public TextField insert_nickname;

    public String getNickname() {
        return insert_nickname.getText();
    }

}
