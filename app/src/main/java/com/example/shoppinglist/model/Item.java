package com.example.shoppinglist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String mName;

    public Item (String name) {
        mName = name;
    }

    protected Item(Parcel in) {
        mName = in.readString();
    }

    public String getName() {
        return mName;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
    }
}
