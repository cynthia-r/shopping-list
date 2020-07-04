package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketItems {
    private List<MarketItem> items;

    //private HashMap<String, Integer> sortMap = new HashMap<>();
    private HashMap<String, MarketItem> itemMap;

    public MarketItems() {
        items = new ArrayList<>();
        itemMap = new HashMap<>();
    }

    public MarketItems(Item[] itemArray) {
        initializeItems(itemArray);
    }

    private void initializeItems(Item[] itemArray) {
        for (int i=0; i<itemArray.length; i++) {
            //sortMap.put(itemArray[i].getName(), i);
            MarketItem marketItem = new MarketItem(itemArray[i], i);
            items.add(marketItem);
            itemMap.put(itemArray[i].getName(), marketItem);
        }
    }

    public int getPosition(String itemName) {
        /*return sortMap.containsKey(itemName)
            ? sortMap.get(itemName) : -1;*/
        return itemMap.containsKey(itemName) ?
                itemMap.get(itemName).getPosition() : -1;
    }

    public Item get(int position) {
        return items.get(position).getItem();
    }

    public int size() {
        return items.size();
    }

    public List<MarketItem> toList() {
        // Return a copy of the list
        return new ArrayList<>(items);
    }

    public void add(Item item) {
        //sortMap.put(item.getName(), items.size());
        MarketItem marketItem = new MarketItem(item, items.size());
        items.add(marketItem);
        itemMap.put(item.getName(), marketItem);
    }

    public void update(int position, String newItemName) {
        MarketItem item = items.get(position);
        //int sort = sortMap.get(item.getName());

        //sortMap.remove(item.getName());

        item.setItemName(newItemName);
        //sortMap.put(newItemName, sort);
    }

    public void remove(int position) {
        MarketItem item = items.get(position);
        items.remove(position);
        itemMap.remove(item.getItemName());
    }

    public void move(int from, int to) {
        if (from == to) {
            return;
        }

        MarketItem itemToMove = items.get(from);
        items.remove(from);
        items.add(to, itemToMove);

        if (from > to) {
            for (int i=to; i<=from; i++) {
                MarketItem item = items.get(i);
                int newIndex = to + (i - to);
                //sortMap.put(item.getName(), newIndex);
                item.setPosition(newIndex);
            }
        }
        else {
            for (int i=to; i>=from; i--) {
                MarketItem item = items.get(i);
                int newIndex = to - (to - i);
                //sortMap.put(item.getName(), newIndex);
                item.setPosition(newIndex);
            }
        }
    }
}
