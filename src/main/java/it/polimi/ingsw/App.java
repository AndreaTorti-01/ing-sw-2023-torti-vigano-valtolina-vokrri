package it.polimi.ingsw;

import it.polimi.ingsw.model.ItemType;
import it.polimi.ingsw.model.PersonalGoalCard;

public class App {
    public static void main(String[] args) {
        PersonalGoalCard personalGoalCard = new PersonalGoalCard(1);
        ItemType[][] pattern = personalGoalCard.getPattern();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                System.out.println(pattern[i][j]);
            }
        }
    }
}
