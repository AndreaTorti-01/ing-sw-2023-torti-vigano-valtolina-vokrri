package it.polimi.ingsw.model;

public class Player {
    // public static List<CommonGoalCard> commonGoalCards;
    private final String name;
    private final Shelf shelf;
    private PersonalGoalCard personalGoalCard;
    private boolean hasAchievedCommonGoalCard;
    private boolean hasAchievedPersonalGoalCard;
    private int score;

    public Player(String name) {
        this.name = name;
        this.shelf = new Shelf();
        this.score = 0;
        this.hasAchievedCommonGoalCard = false;
        this.hasAchievedPersonalGoalCard = false;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public PersonalGoalCard getPersonalGoalCard() {
        return this.personalGoalCard;
    }

    public void setPersonalGoalCard(PersonalGoalCard personalGoalCard) {
        this.personalGoalCard = personalGoalCard;
    }

    public Shelf getShelf() {
        return this.shelf;
    }

    public String getName() {
        return name;
    }

    public boolean hasAchievedCommonGoalCard() {
        return hasAchievedCommonGoalCard;
    }

    public void setAchievedCommonGoalCard() {
        this.hasAchievedCommonGoalCard = true;
    }

    public boolean hasAchievedPersonalGoalCard() {
        return hasAchievedPersonalGoalCard;
    }

    public void setAchievedPersonalGoalCard() {
        this.hasAchievedPersonalGoalCard = true;
    }
}
