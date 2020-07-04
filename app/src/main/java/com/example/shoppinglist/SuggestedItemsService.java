package com.example.shoppinglist;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.model.ShoppingList;

import java.util.ArrayList;
import java.util.List;

public class SuggestedItemsService {

    private MarketItems mMarketItems;

    public SuggestedItemsService(MarketItems marketItems) {
        mMarketItems = marketItems;
    }

    public ShoppingList getSuggestedItems(ShoppingList shoppingList) {

        List<Item> itemList = new ArrayList<>();
        int i=0;
        int j=0;
        while (i < 5 && j < mMarketItems.size()) { // Get first 5 for now
            Item item = mMarketItems.get(j);
            if (!shoppingList.contains(item.getName())) {
                itemList.add(item);
                i++;
            }
            j++;
        }

        ShoppingList suggestedShoppingList = new ShoppingList(mMarketItems);
        for (Item item : itemList) {
            suggestedShoppingList.add(item, 1, false);
        }

        return suggestedShoppingList;
    }
}
