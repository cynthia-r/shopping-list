package com.example.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;

import java.util.List;

public class ShoppingListViewAdapter extends RecyclerView.Adapter<ShoppingListViewAdapter.ShoppingListViewHolder> {

    private List<Item> mData;
    private LayoutInflater mInflater;
    private OnItemCheckListener mOnItemCheckListener;

    // data is passed into the constructor
    ShoppingListViewAdapter(Context context, List<Item> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_row, parent, false);
        return new ShoppingListViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ShoppingListViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.myTextView.setText(item.getName());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkbox;
        TextView myTextView;

        ShoppingListViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.item);

            // Bind checkbox changes to onclick events
            // So that the user can click anywhere on the item to select/un-select it
            itemView.setOnClickListener(this);
            checkbox = itemView.findViewById(R.id.checkbox);
            checkbox.setClickable(false);
        }

        @Override
        public void onClick(View view) {

            if (mOnItemCheckListener != null) {
                checkbox.setChecked(!checkbox.isChecked());
                if (checkbox.isChecked()) {
                    mOnItemCheckListener.onItemCheck(getAdapterPosition());
                }
                else {
                    mOnItemCheckListener.onItemUncheck(getAdapterPosition());
                }
            }
        }
    }

    // convenience method for getting data at click position
    Item getItem(int id) {
        return mData.get(id);
    }

    // allows check events to be caught
    void setItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.mOnItemCheckListener = onItemCheckListener;
    }

    // parent activity will implement this method to respond to check events
    public interface OnItemCheckListener {
        void onItemCheck(int position);
        void onItemUncheck(int position);
    }
}
