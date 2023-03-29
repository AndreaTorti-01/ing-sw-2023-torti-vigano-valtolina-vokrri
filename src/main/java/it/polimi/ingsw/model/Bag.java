package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Bag extends GameObject {
    private final int maxCards = Constants.maxNumberOfCards;
    private final Set<ItemCard> cardsInside;

    /**
     * The bag is filled with 132 cards when instantiated
     */
    public Bag() {
        cardsInside = new HashSet<>();
        for (ItemType type : ItemType.values()) {
            for (int i = 0; i < Constants.numberOfCardWithSameType; i++) {
                cardsInside.add(new ItemCard(type));
            }
        }
    }

    /**
     * @return Set of cards that are inside the bag
     */
    public Set<ItemCard> getCardsInside() {
        return cardsInside;
    }

    /**
     * Draw a RANDOM card from the bag
     *
     * @return ItemCard | null, depending on the presence of cards in the bag
     */
    public ItemCard drawCard() {
        if (cardsInside.size() > 0) {
            ItemCard itemCard = cardsInside.stream().skip(
                    new Random().nextInt(cardsInside.size())
            ).findFirst().orElse(null);
            cardsInside.remove(itemCard);
            return itemCard;
        } else return null;
    }

}