package com.example.shoppinglist.model;

public class PreviouslyBoughtItem {
    private Item mItem;
    private int mLastBought;

    public PreviouslyBoughtItem(Item item, int lastBought) {
        mItem = item;
        mLastBought = lastBought;
    }

    public Item getItem() {
        return mItem;
    }

    public String getItemName() {
        return mItem.getName();
    }

    public int getLastBought() {
        return mLastBought;
    }

    public void setItemName(String itemName) {
        mItem.setName(itemName);
    }

    public void setLastBought(int lastBought) {
        this.mLastBought = lastBought;
    }
}
