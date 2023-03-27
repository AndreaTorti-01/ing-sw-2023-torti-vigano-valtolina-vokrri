package it.polimi.ingsw.model;
import java.util.*;

public class Bag extends GameObject {
    private Set<ItemCard> cardsInside;
    private final int maxCards = 132;

    /**
     * The bag is filled with 132 cards when instantiated
     */
    public Bag() {
        cardsInside = new HashSet<ItemCard>();
        for (int i = 0; i < maxCards; i += 6) {
            ItemCard ic;
            cardsInside.add(ic = new ItemCard(ItemType.BOOKS));
            cardsInside.add(ic = new ItemCard(ItemType.GAMES));
            cardsInside.add(ic = new ItemCard(ItemType.PLANTS));
            cardsInside.add(ic = new ItemCard(ItemType.TROPHIES));
            cardsInside.add(ic = new ItemCard(ItemType.FRAMES));
            cardsInside.add(ic = new ItemCard(ItemType.CATS));

        }
    }

    /**
     *
     * @return Set of cards that are inside the bag
     */
    public Set<ItemCard> getCardsInside() {
        return cardsInside;
    }

    /**
     * Draw a RANDOM card from the bag
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