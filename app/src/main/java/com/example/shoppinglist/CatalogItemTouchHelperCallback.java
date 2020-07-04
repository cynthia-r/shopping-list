package com.example.shoppinglist;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class CatalogItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public CatalogItemTouchHelperCallback() {

    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        CatalogListViewAdapter adapter = (CatalogListViewAdapter) recyclerView.getAdapter();

        int from = viewHolder.getAdapterPosition();
        int to = target.getAdapterPosition();

        adapter.moveItem(from, to);

        adapter.notifyItemMoved(from, to);

        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        // Not using swipe feature
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    // Override isItemViewSwipeEnabled if we want to support swiping later on
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                  int actionState) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            CatalogListViewAdapter.CatalogListViewHolder holder = (CatalogListViewAdapter.CatalogListViewHolder)viewHolder;
            holder.onSelectedItem();
        }

        super.onSelectedChanged(viewHolder, actionState);
    }
    @Override
    public void clearView(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder) {

        CatalogListViewAdapter.CatalogListViewHolder holder = (CatalogListViewAdapter.CatalogListViewHolder)viewHolder;
        holder.onItemCleared();

        super.clearView(recyclerView, viewHolder);
    }
}
