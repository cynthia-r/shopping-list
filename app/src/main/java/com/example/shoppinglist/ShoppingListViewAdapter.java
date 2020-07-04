package com.example.shoppinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;

public class ShoppingListViewAdapter extends RecyclerView.Adapter<ShoppingListViewAdapter.ShoppingListViewHolder> {

    private ShoppingList mData;
    private LayoutInflater mInflater;
    private OnItemCheckListener mOnItemCheckListener;
    private OnLongClickListener mOnLongClickListener;

    // data is passed into the constructor
    ShoppingListViewAdapter(Context context, ShoppingList data) {
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
        ShoppingListItem item = mData.get(position);
        holder.itemNameTextView.setText(item.getItemName());
        int quantity = item.getQuantity();
        if (quantity > 1) {
            holder.quantityTextView.setText(" x" + quantity);
        }
        else {
            holder.quantityTextView.setText("");
        }
        holder.checkbox.setChecked(item.isSelected());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    public ShoppingListItem getItem(int id) {
        return mData.get(id);
    }

    // allows check events to be caught
    public void setItemCheckListener(OnItemCheckListener onItemCheckListener) {
        this.mOnItemCheckListener = onItemCheckListener;
    }

    // allows check events to be caught
    public void setLongClickListener(OnLongClickListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
    }

    // parent activity will implement this method to respond to check events
    public interface OnItemCheckListener {
        void onItemCheck(int position);
        void onItemUncheck(int position);
    }

    public interface OnLongClickListener {
        void onLongClick(ShoppingListItem item);
    }

    // stores and recycles views as they are scrolled off screen
    public class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CheckBox checkbox;
        TextView itemNameTextView;
        TextView quantityTextView;

        ShoppingListViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.item);
            quantityTextView = itemView.findViewById(R.id.quantity);

            // Bind checkbox changes to onclick events
            // So that the user can click anywhere on the item to select/un-select it
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            itemView.setOnTouchListener(new OnItemTouchListener());
            checkbox = itemView.findViewById(R.id.checkbox);
            checkbox.setClickable(false);
        }

        @Override
        public void onClick(View view) {

            if (mOnItemCheckListener != null) {
                checkbox.setChecked(!checkbox.isChecked());
                int position = getAdapterPosition();
                if (checkbox.isChecked()) {
                    mOnItemCheckListener.onItemCheck(position);
                }
                else {
                    mOnItemCheckListener.onItemUncheck(position);
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            ShoppingListItem item = getItem(position);
            mOnLongClickListener.onLongClick(item);
            return true;
        }
    }
}
