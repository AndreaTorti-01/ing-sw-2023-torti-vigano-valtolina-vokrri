package it.polimi.ingsw.model;

import it.polimi.ingsw.utils.Constants;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static it.polimi.ingsw.utils.Constants.maxNumberOfItemCards;
import static it.polimi.ingsw.utils.Constants.numberOfItemCardsWithSameType;

public class Bag {
    private final int maxCards = maxNumberOfItemCards;
    private final Set<ItemCard> cardsInside;

    /**
     * Fills the bag with every type of ItemCard.
     * See {@link Constants} to know the number of cards inside the bag and the number of ItemCards with the same type.
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
     * @return the set of cards contained in the bag
     */
    public Set<ItemCard> getCardsInside() {
        return cardsInside;
    }

    /**
     * @return a random ItemCard from the bag, if not empty
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