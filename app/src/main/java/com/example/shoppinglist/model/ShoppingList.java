package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShoppingList {
    private SortedMap<String, Item> itemMap;
    private Set<String> selectedItems = new HashSet<>();

    public ShoppingList() {
        itemMap = new TreeMap<>(new MarketItemComparator());
    }

    public boolean contains(String itemName) {
        return itemMap.containsKey(itemName);
    }

    public void add(Item item) {
        itemMap.put(item.getName(), item);
    }

    public void select(String itemName) {
        selectedItems.add(itemName);
    }

    public void deselect(String itemName) {
        selectedItems.remove(itemName);
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

    public boolean isSelected(String itemName) {
        return selectedItems.contains(itemName);
    }

    public List<String> toNameList(boolean selectedOnly) {
        List<String> items = new ArrayList<>();
        Iterator<String> it = itemMap.keySet().iterator();
        while (it.hasNext()) {
            String itemName = it.next();
            if (!selectedOnly || selectedItems.contains(itemName)) {
                items.add(itemName);
            }
        }

        return items;
    }

    public List<Item> toList(boolean selectedOnly) {
        List<Item> items = new ArrayList<>();
        Iterator<String> it = itemMap.keySet().iterator();
        while (it.hasNext()) {
            String itemName = it.next();
            if (!selectedOnly || selectedItems.contains(itemName)) {
                items.add(itemMap.get(itemName));
            }
        }

        return items;
    }

    public void clearUnselected() {

        Set<String> allKeys = itemMap.keySet();
        List<String> keysToRemove = new ArrayList<>();

        for (String itemName : allKeys) {
            if (!isSelected(itemName)) {
                keysToRemove.add(itemName);
            }
        }

        for (String i : keysToRemove) {
            itemMap.remove(i);
        }

        // TODO clear all selected items as well
        selectedItems.clear();
    }
}
