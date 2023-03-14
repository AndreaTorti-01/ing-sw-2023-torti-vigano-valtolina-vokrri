package main.java.it.polimi.ingsw.model;

public class Player {
    private String nickname;
    private int score;
    private PersonalGoalCard personalGoalCard;

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return personalGoalCard;
    }
}
