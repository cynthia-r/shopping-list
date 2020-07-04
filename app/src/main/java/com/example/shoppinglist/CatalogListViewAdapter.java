package com.example.shoppinglist;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;

public class CatalogListViewAdapter extends RecyclerView.Adapter<CatalogListViewAdapter.CatalogListViewHolder> {
    private MarketItems mData;
    private LayoutInflater mInflater;
    private OnLongClickListener mOnLongClickListener;
    private OnStartDragListener mOnDragStartListener;

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

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        mOnLongClickListener = onLongClickListener;
    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        mOnDragStartListener = onStartDragListener;
    }

    // stores and recycles views as they are scrolled off screen
    public class CatalogListViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnTouchListener {
        TextView textView;
        ImageView handleView;

        CatalogListViewHolder(final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item);
            handleView = itemView.findViewById(R.id.handle);
            itemView.setOnLongClickListener(this);
            itemView.setOnTouchListener(new OnCatalogItemTouchListener());
            handleView.setOnTouchListener(this);
        }

        public void onSelectedItem() {
            textView.setAlpha(0.5f);
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        public void onItemCleared() {
            textView.setAlpha(1.0f);
            itemView.setBackgroundColor(0);
        }

        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            Item item = get(position);
            mOnLongClickListener.onLongClick(item, position);

            textView.setAlpha(1.0f);
            itemView.setBackgroundColor(0);
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            textView.setAlpha(0.5f);
            itemView.setBackgroundColor(Color.LTGRAY);

            if (MotionEventCompat.getActionMasked(event) ==
                    MotionEvent.ACTION_DOWN) {
                mOnDragStartListener.onStartDrag(this);
            }
            return false;
        }
    }

    public interface OnLongClickListener {
        void onLongClick(Item item, int position);
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public class OnCatalogItemTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            TextView textView = view.findViewById(R.id.item);

            int motionAction = MotionEventCompat.getActionMasked(motionEvent);

            if (motionAction ==
                    MotionEvent.ACTION_DOWN) {
                textView.setAlpha(0.5f);
                view.setBackgroundColor(Color.LTGRAY);
            }
            /*switch (MotionEventCompat.getActionMasked(motionEvent)) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_SCROLL:
                case MotionEvent.ACTION_MOVE:*/
            if (motionAction == MotionEvent.ACTION_UP || motionAction == MotionEvent.ACTION_CANCEL) {
                    textView.setAlpha(1.0f);
                    textView.setAlpha(1.0f);
                    view.setBackgroundColor(0);
                    /*break;
                default:break;*/
            }

            return false;
        }
    }
}
