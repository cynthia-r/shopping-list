package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketItems {
    private List<Item> items = new ArrayList<>();

    private HashMap<String, Integer> sortMap = new HashMap<>();

    public MarketItems() {

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

    public void update(int position, String newItemName) {
        Item item = items.get(position);
        int sort = sortMap.get(item.getName());

        sortMap.remove(item.getName());

        item.setName(newItemName);
        sortMap.put(newItemName, sort);
    }

    public void remove(int position) {
        items.remove(position);
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
