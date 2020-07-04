package com.example.shoppinglist.model;

import java.util.Comparator;

public class PreviouslyBoughtItemComparator implements Comparator<String> {

    private PreviouslyBoughtItems mPreviouslyBoughtItems;

    public PreviouslyBoughtItemComparator(PreviouslyBoughtItems previouslyBoughtItems) {
        mPreviouslyBoughtItems = previouslyBoughtItems;
    }

    @Override
    public int compare(String s, String t1) {
        int sPos = mPreviouslyBoughtItems.contains(s) ? mPreviouslyBoughtItems.get(s).getLastBought() : -1;
        int t1Pos = mPreviouslyBoughtItems.contains(t1) ? mPreviouslyBoughtItems.get(t1).getLastBought() : -1;

        return sPos != t1Pos ? sPos - t1Pos : s.compareTo(t1);
    }
}
