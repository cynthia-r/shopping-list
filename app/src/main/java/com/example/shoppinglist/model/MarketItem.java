package com.example.shoppinglist.model;

public class MarketItem {
    private Item mItem;
    private int mPosition;

    public MarketItem(Item item, int position) {
        mItem = item;
        mPosition = position;
    }

    public Item getItem() {
        return mItem;
    }

    public String getItemName() {
        return mItem.getName();
    }

    public int getPosition() {
        return mPosition;
    }

    public void setItemName(String itemName) {
        mItem.setName(itemName);
    }

    public void setPosition(int position) {
        this.mPosition = position;
    }
}
