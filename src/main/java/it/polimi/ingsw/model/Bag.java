package it.polimi.ingsw.model;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Bag extends GameObject {
    private final int maxCards = 132;
    private Set<ItemCard> cardsInside;

    /**
     * The bag is filled with 132 cards when instantiated
     */
    public Bag() {
        cardsInside = new HashSet<>();
        for (ItemType type : ItemType.values()) {
            // 22 the number of cards with the same type in the bag
            for (int i = 0; i < 22; i++) {
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
            ItemCard ic_2;
            ic_2 = cardsInside.stream().skip(new Random().nextInt(cardsInside.size())).findFirst().orElse(null);
            cardsInside.remove(ic_2);
            return ic_2;
        } else return null;
    }

}