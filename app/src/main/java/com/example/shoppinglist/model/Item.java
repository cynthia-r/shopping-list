package com.example.shoppinglist.model;

public class Item {
    private String mName;

    public Item (String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
