package com.example.shoppinglist.model;

import java.util.HashMap;

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

    private HashMap<String, Integer> sortMap = new HashMap<>();

    public MarketItems() {
        for (int i=0; i<marketItems.length; i++) {
            sortMap.put(marketItems[i].getName(), i);
        }
    }

    public int getPosition(String itemName) {
        return sortMap.containsKey(itemName)
            ? sortMap.get(itemName) : -1;
    }
}
