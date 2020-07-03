package com.example.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingListItem;

import java.util.List;

public class ConfirmListViewAdapter extends RecyclerView.Adapter<ConfirmListViewAdapter.ConfirmListViewHolder> {
    private List<ShoppingListItem> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    public ConfirmListViewAdapter(Context context, List<ShoppingListItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ConfirmListViewAdapter.ConfirmListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.confirm_list_row, parent, false);
        return new ConfirmListViewAdapter.ConfirmListViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ConfirmListViewAdapter.ConfirmListViewHolder holder, int position) {
        ShoppingListItem item = mData.get(position);
        holder.myTextView.setText(item.getItemName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ConfirmListViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        public ConfirmListViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.item);
        }
    }
}
