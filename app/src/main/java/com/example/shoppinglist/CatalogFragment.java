package com.example.shoppinglist;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.service.FileService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment implements AddCatalogItemFragment.AddCatalogItemDialogListener, EditCatalogFragment.EditCatalogItemDialogListener,
        CatalogListViewAdapter.OnStartDragListener {

    public static final String TAG = "Catalog";

    private CatalogListViewAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private MarketItems marketItems;

    public CatalogFragment() {
        // Required empty public constructor
    }

    public static CatalogFragment newInstance() {
        CatalogFragment fragment = new CatalogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        MainActivity activity = (MainActivity)getActivity();

        FileService fileService = new FileService(getContext());
        marketItems = fileService.readMarketItems(ListConstants.CATALOG);

        // set up the shopping list RecyclerView
        RecyclerView recyclerView = activity.findViewById(R.id.catalog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new CatalogListViewAdapter(activity, marketItems);
        adapter.setOnLongClickListener(activity);
        adapter.setOnStartDragListener(this);
        recyclerView.setAdapter(adapter);

        // Set up the Add button
        FloatingActionButton addButton = activity.findViewById(R.id.add);
        addButton.setOnClickListener(activity);

        mItemTouchHelper = new ItemTouchHelper(new CatalogItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop () {

        String filename = ListConstants.CATALOG;
        FileService fileService = new FileService(getContext());
        fileService.saveMarketItems(filename, marketItems);

        super.onStop();
    }

    @Override
    public void onItemAdd(String inputText) {
        Item newItem = new Item(inputText);
        marketItems.add(newItem);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemEdit(int position, String newItemName) {
        marketItems.update(position, newItemName);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(int position) {
        marketItems.remove(position);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
