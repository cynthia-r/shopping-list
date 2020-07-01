package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketItems {
    private List<Item> items = new ArrayList<>();

    // TODO move to file
    private Item[] marketItems = new Item[] {
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
        //initializeItems(marketItems);
    }

    public MarketItems(Item[] itemArray) {
        initializeItems(itemArray);
    }

    private void initializeItems(Item[] itemArray) {
        for (int i=0; i<itemArray.length; i++) {
            sortMap.put(itemArray[i].getName(), i);
            items.add(itemArray[i]);
        }
    }

    public int getPosition(String itemName) {
        return sortMap.containsKey(itemName)
            ? sortMap.get(itemName) : -1;
    }

    public Item get(int position) {
        return items.get(position);
    }

    public int size() {
        return items.size();
    }

    public List<Item> toList() {
        // Return a copy of the list
        return new ArrayList<>(items);
    }

    public void add(Item item) {
        sortMap.put(item.getName(), items.size());
        items.add(item);
    }

    public void move(int from, int to) {
        if (from == to) {
            return;
        }

        Item itemToMove = items.get(from);
        items.remove(from);
        items.add(to, itemToMove);

        if (from > to) {
            for (int i=to; i<=from; i++) {
                Item item = items.get(i);
                int newIndex = to + (i - to);
                sortMap.put(item.getName(), newIndex);
            }
        }
        else {
            for (int i=to; i>=from; i--) {
                Item item = items.get(i);
                int newIndex = to - (to - i);
                sortMap.put(item.getName(), newIndex);
            }
        }
    }
}
