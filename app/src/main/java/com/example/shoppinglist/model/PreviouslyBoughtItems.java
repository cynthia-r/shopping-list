package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PreviouslyBoughtItems {

    private List<PreviouslyBoughtItem> items;
    private HashMap<String, PreviouslyBoughtItem> itemMap;

    public PreviouslyBoughtItems() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
    }

    public boolean contains(String itemName) {
        return itemMap.containsKey(itemName);
    }

    public PreviouslyBoughtItem get(String itemName) {
        return itemMap.get(itemName);
    }

    public void add(Item item) {
        PreviouslyBoughtItem previouslyBoughtItem = new PreviouslyBoughtItem(item, 1);
        items.add(previouslyBoughtItem);
        itemMap.put(item.getName(), previouslyBoughtItem);
    }

    public void add(PreviouslyBoughtItem item) {
        // Add or increment lastBought counter
        String itemName = item.getItemName();
        items.add(item);
        itemMap.put(itemName, item);
    }

    public void update(String itemName, int lastBought) {
        itemMap.get(itemName).setLastBought(lastBought);
    }

    public void incrementExisting() {
        for (PreviouslyBoughtItem item : items) {
            int lastBought = item.getLastBought();
            if (lastBought > 0) {
                item.setLastBought(lastBought + 1);
            }
        }
    }

    public void clearOldItems() {
        int i=0;
        while (i<items.size()) {
            PreviouslyBoughtItem item = items.get(i);
            if (item.getLastBought() > 3) {
                items.remove(i);
                itemMap.remove(item.getItemName());
            }
            else {
                i++;
            }
        }
    }

    public List<PreviouslyBoughtItem> toList() {
        return items;
    }

    /*public int getPosition(String itemName) {
        return -1;
    }*/
}
