package com.example.shoppinglist;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogFragment extends Fragment implements AddCatalogItemFragment.AddCatalogItemDialogListener {

    private CatalogListViewAdapter adapter;
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
        marketItems = fileService.readMarketItems("catalog");

        // set up the shopping list RecyclerView
        RecyclerView recyclerView = activity.findViewById(R.id.catalog_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new CatalogListViewAdapter(activity, marketItems);
        recyclerView.setAdapter(adapter);

        // Set up the Add button
        FloatingActionButton addButton = activity.findViewById(R.id.add);
        addButton.setOnClickListener(activity);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new CatalogItemTouchHelperCallback());

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop () {

        String filename = "catalog";
        FileService fileService = new FileService(getContext());
        fileService.saveMarketItems(filename, marketItems);

        super.onStop();
    }

    @Override
    public void onItemAdd(String inputText) {
        Item newItem = new Item(inputText);
        marketItems.add(newItem);
    }
}
