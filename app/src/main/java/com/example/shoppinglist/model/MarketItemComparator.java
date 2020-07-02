package com.example.shoppinglist.model;

import java.util.Comparator;

public class MarketItemComparator implements Comparator<String> {

    private MarketItems marketItems;

    public MarketItemComparator(MarketItems items) {
        marketItems = items;
    }

    @Override
    public int compare(String s, String t1) {
        int sPos = marketItems.getPosition(s);
        int t1Pos = marketItems.getPosition(t1);

        return sPos != t1Pos ? sPos - t1Pos : s.compareTo(t1);
    }
}
