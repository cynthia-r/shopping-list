package com.example.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItemComparator;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.model.ShoppingList;

public class CatalogListViewAdapter extends RecyclerView.Adapter<CatalogListViewAdapter.CatalogListViewHolder> {
    private MarketItems mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    CatalogListViewAdapter(Context context, MarketItems data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public CatalogListViewAdapter.CatalogListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.catalog_row, parent, false);
        return new CatalogListViewAdapter.CatalogListViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final CatalogListViewAdapter.CatalogListViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.textView.setText(item.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    Item get(int position) {
        return mData.get(position);
    }

    public void moveItem(int from, int to) {
        mData.move(from, to);
    }

    // stores and recycles views as they are scrolled off screen
    public class CatalogListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        CatalogListViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
        }

        public void onSelectedItem() {
            textView.setAlpha(0.5f);
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        public void onItemCleared() {
            textView.setAlpha(1.0f);
            itemView.setBackgroundColor(0);
        }
    }
}
