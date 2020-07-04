package com.example.shoppinglist.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class ShoppingList {
    private SortedMap<String, ShoppingListItem> itemMap;

    public ShoppingList(Comparator<String> itemComparator) {
        itemMap = new TreeMap<>(itemComparator);
    }

    public boolean contains(String itemName) {
        return itemMap.containsKey(itemName);
    }

    public void add(Item item, int quantity, boolean isSelected) {
        itemMap.put(item.getName(), new ShoppingListItem(item, quantity, isSelected));
    }

    public void add(ShoppingListItem item) {
        itemMap.put(item.getItemName(), item);
    }

    public void update(String itemName, int quantity) {
        itemMap.get(itemName).setQuantity(quantity);
    }

    public void remove(String itemName) {
        itemMap.remove(itemName);
    }

    public void select(String itemName) {
        itemMap.get(itemName).setIsSelected(true);
    }

    public void deselect(String itemName) {
        itemMap.get(itemName).setIsSelected(false);
    }

    public ShoppingListItem get(int position) {
        Iterator<String> it = itemMap.keySet().iterator();
        int i=0;
        while (i < position && it.hasNext()) {
            i++;
            it.next();
        }

        return it.hasNext() ? itemMap.get(it.next()) : null;
    }

    public int getPosition(String itemName) {
        if (!itemMap.containsKey(itemName)) {
            return -1;
        }

        int position = 0;
        Iterator<String> it = itemMap.keySet().iterator();
        while (it.hasNext()) {
            String currentItemName = it.next();
            if (currentItemName.equals(itemName)) {
                break;
            }
            position++;
        }
        return position;
    }

    public int size() {
        return itemMap.size();
    }

    public void clear() {
        itemMap.clear();
    }

    public void addAll(ShoppingList shoppingList, boolean selectedOnly) {
        for (ShoppingListItem item : shoppingList.toList(selectedOnly)) {
            itemMap.put(item.getItem().getName(), item);
        }
    }

    public List<ShoppingListItem> toList(boolean selectedOnly) {
        List<ShoppingListItem> items = new ArrayList<>();
        Iterator<String> it = itemMap.keySet().iterator();
        while (it.hasNext()) {
            String itemName = it.next();
            ShoppingListItem item = itemMap.get(itemName);
            if (!selectedOnly || item.isSelected()) {
                items.add(itemMap.get(itemName));
            }
        }

        return items;
    }

    public void clearUnselected() {

        Set<String> allKeys = itemMap.keySet();
        List<String> keysToRemove = new ArrayList<>();

        for (String itemName : allKeys) {
            if (!itemMap.get(itemName).isSelected()) {
                keysToRemove.add(itemName);
            }
        }

        for (String i : keysToRemove) {
            itemMap.remove(i);
        }
    }
}
