package it.polimi.ingsw.model;

import it.polimi.ingsw.model.ItemCards.ItemCard;
import it.polimi.ingsw.model.ItemCards.ItemType;
import it.polimi.ingsw.utils.Constants;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.numberOfItemCardsWithSameType;

/**
 * A class that represents the Bag.
 * This class contains the Item Cards used to fill the Board.
 */
public class Bag implements Serializable {
    @Serial
    private static final long serialVersionUID = -141148278954951272L;
    /**
     * The set of cards contained in this bag.
     */
    private final Set<ItemCard> cardsInside;

    /**
     * Fills this bag with every type of ItemCard.
     * See {@link Constants} to know the number of cards inside this bag
     * and the number of Item Cards with the same type.
     */
    public Bag() {
        cardsInside = new HashSet<>();
        for (ItemType type : ItemType.values()) {
            for (int i = 0; i < numberOfItemCardsWithSameType; i++) {
                cardsInside.add(new ItemCard(type, i % 3));
            }
        }
    }

    /**
     * Gets the set of cards contained in this bag.
     *
     * @return the set of cards contained in this bag.
     */
    public Set<ItemCard> getCardsInside() {
        return cardsInside;
    }

    /**
     * Draws a random ItemCard from this bag.
     *
     * @return a random ItemCard from this bag, if not empty.
     */
    public ItemCard drawCard() {
        if (cardsInside.size() == 0) return null;

        ItemCard itemCard = cardsInside.stream().skip(
                new Random().nextInt(cardsInside.size())
        ).findFirst().orElse(null);
        cardsInside.remove(itemCard);
        return itemCard;
    }

}