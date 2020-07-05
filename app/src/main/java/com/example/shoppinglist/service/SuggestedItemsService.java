package com.example.shoppinglist.service;

import com.example.shoppinglist.model.PreviouslyBoughtItem;
import com.example.shoppinglist.model.PreviouslyBoughtItemComparator;
import com.example.shoppinglist.model.PreviouslyBoughtItems;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;

import java.util.List;

public class SuggestedItemsService {

    private PreviouslyBoughtItems mPreviouslyBoughtItems;

    public SuggestedItemsService(PreviouslyBoughtItems previouslyBoughtItems) {
        mPreviouslyBoughtItems = previouslyBoughtItems;
    }

    public ShoppingList getSuggestedItems(ShoppingList shoppingList) {

        PreviouslyBoughtItemComparator itemComparator = new PreviouslyBoughtItemComparator(mPreviouslyBoughtItems);
        ShoppingList suggestedShoppingList = new ShoppingList(itemComparator);

        for (PreviouslyBoughtItem previouslyBoughtItem : mPreviouslyBoughtItems.toList()) {
            if (!shoppingList.contains(previouslyBoughtItem.getItemName())) {
                suggestedShoppingList.add(previouslyBoughtItem.getItem(), 1, false);
            }
        }

        return suggestedShoppingList;
    }

    public void updateBoughtItems(List<ShoppingListItem> items) {

        // Increment last bought by 1 for all items in previously bought items
        mPreviouslyBoughtItems.incrementExisting();

        for (ShoppingListItem item : items) {
            String itemName = item.getItemName();

            // Update last bought to 1 for all shopping items that are selected
            if (item.isSelected()) {
                if (mPreviouslyBoughtItems.contains(itemName)) {
                    mPreviouslyBoughtItems.update(itemName, 1);
                }
                else {
                    mPreviouslyBoughtItems.add(item.getItem());
                }
            }
            // Update last bought to 2 for all shopping items that are not selected
            else {
                mPreviouslyBoughtItems.update(itemName, 2);
            }
        }

        // Remove any item in previously bought that has lastBought > 3
        mPreviouslyBoughtItems.clearOldItems();
    }
}
