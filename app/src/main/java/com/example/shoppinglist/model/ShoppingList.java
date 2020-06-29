package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShoppingList {
    private SortedMap<String, Item> itemMap;

    public ShoppingList() {
        itemMap = new TreeMap<>(new MarketItemComparator());
    }

    public boolean contains(String itemName) {
        return itemMap.containsKey(itemName);
    }

    public void add(Item item) {
        itemMap.put(item.getName(), item);
    }

    public Item get(int position) {
        Iterator<String> it = itemMap.keySet().iterator();
        int i=0;
        while (i < position && it.hasNext()) {
            i++;
            it.next();
        }

        return it.hasNext() ? itemMap.get(it.next()) : null;
    }

    public int size() {
        return itemMap.size();
    }

    public List<String> toList() {
        List<String> items = new ArrayList<>();
        Iterator<String> it = itemMap.keySet().iterator();
        while (it.hasNext()) {
            items.add(it.next());
        }

        return items;
    }
}
