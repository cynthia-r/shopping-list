package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.List;

public class MarketItems {
    //public List<Item> items = new ArrayList<>();
    public Item[] marketItems = new Item[] {
            new Item("Barres"),
            new Item("Cereales cuicui"),
            new Item("Echalotes"),
            new Item("Salade cuicui"),
            new Item("Eau gallon"),
            new Item("Oeufs"),
            new Item("Yaourt doudou"),
            new Item("Petits pains"),
            new Item("Fromage"),
            new Item("Lardons")
    };

    public MarketItems() {

        /*for (Item item : marketItems) {
            items.add(item);
        }*/
    }
}
