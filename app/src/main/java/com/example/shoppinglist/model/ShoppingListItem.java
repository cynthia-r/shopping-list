package com.example.shoppinglist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ShoppingListItem implements Parcelable {
    private Item mItem;
    private boolean mIsSelected;
    private int mQuantity;

    public ShoppingListItem(Item item, int quantity, boolean isSelected) {
        mItem = item;
        mIsSelected = isSelected;
        mQuantity = quantity;
    }

    protected ShoppingListItem(Parcel in) {
        mItem = new Item(in.readString());
        mQuantity = in.readInt();
    }

    public Item getItem() {
        return mItem;
    }

    public String getItemName() {
        return mItem.getName();
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public static final Creator<ShoppingListItem> CREATOR = new Creator<ShoppingListItem>() {
        @Override
        public ShoppingListItem createFromParcel(Parcel in) {
            return new ShoppingListItem(in);
        }

        @Override
        public ShoppingListItem[] newArray(int size) {
            return new ShoppingListItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getItemName());
        parcel.writeInt(mQuantity);
    }
}
