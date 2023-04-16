package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Observable;
import it.polimi.ingsw.utils.Observer;

import java.util.ArrayList;

public class GameView extends Observable<String> implements Observer<Game, String> {
    private final Game model;

    @Override
    public void update(Game o, String arg) {
        setChanged();
        notifyObservers(arg);
    }

    public GameView (Game model) {
        if (model == null) throw new IllegalArgumentException();
        this.model = model;
        model.addObserver(this);
    }

    // TODO return board or what?
    public ItemCard[][] getBoard() {
        return model.getBoard().getTileMatrix();
    }

    // TODO return board or what?
    public boolean[][] getBoardValid() {
        return model.getBoard().getValidMatrix();
    }

    // TODO return shelf?
    public ItemCard[][] getShelf() {
        return model.getCurrentPlayer().getShelf().getItemsMatrix();
    }

    // TODO same as above: has setter methods
    public ArrayList<CommonGoalCard> getCommonGoalCards() {
        return model.getCommonGoalCards();
    }

    public Player getCurrentPlayer() {
        return model.getCurrentPlayer();
    }



}
