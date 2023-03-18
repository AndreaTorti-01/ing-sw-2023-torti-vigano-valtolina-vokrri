package it.polimi.ingsw.model;
import java.util.*;

public class Bag extends GameObject {
    private Set<ItemCard> cardsInside;
    private int maxCards=132;

    public Bag() {
        cardsInside = new HashSet<ItemCard>();
        for(int i =0; i<maxCards ;i+=6){
            ItemCard ic;
            cardsInside.add(ic = new ItemCard(ItemType.BOOKS));
            cardsInside.add(ic = new ItemCard(ItemType.GAMES));
            cardsInside.add(ic = new ItemCard(ItemType.PLANTS));
            cardsInside.add(ic = new ItemCard(ItemType.TROPHIES));
            cardsInside.add(ic = new ItemCard(ItemType.FRAMES));
            cardsInside.add(ic = new ItemCard(ItemType. CATS));

        }
    }

    public Set<ItemCard> getCardsInside() {
        return cardsInside;
    }

    public ItemCard drawCard(Set<ItemCard> cardsInside) {
        if(cardsInside.size()>0){
            ItemCard ic_2;
            ic_2= getRandomSetElement(cardsInside);
            cardsInside.remove(ic_2);
            return ic_2;
        }
        else return null;  //Da aggiungere exception(?)
    }

    public static <Itemcard> Itemcard getRandomSetElement(Set<Itemcard> set) {
        return set.stream().skip(new Random().nextInt(set.size())).findFirst().orElse(null);
    }
}