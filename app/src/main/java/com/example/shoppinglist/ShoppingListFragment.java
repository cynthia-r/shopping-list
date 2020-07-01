package com.example.shoppinglist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingListViewAdapter.OnItemCheckListener, AddFragment.AddItemDialogListener {

    private ShoppingListViewAdapter adapter;
    private ShoppingList shoppingList;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
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
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        MainActivity activity = (MainActivity)getActivity();

        String filename = "listFile";
        FileService fileService = new FileService(activity);
        shoppingList = fileService.openFile(filename);

        // set up the shopping list RecyclerView
        RecyclerView recyclerView = activity.findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShoppingListViewAdapter(activity, shoppingList);
        adapter.setItemCheckListener(this);
        recyclerView.setAdapter(adapter);

        // Set up the Add button
        FloatingActionButton addButton = activity.findViewById(R.id.add);
        addButton.setOnClickListener(activity);

        // Set up the Done button
        Button button = activity.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop () {
        String filename = "listFile";
        FileService fileService = new FileService(getContext());
        fileService.writeToFile(filename, shoppingList);

        super.onStop();
    }

    @Override
    public void onItemCheck(int position) {
        Item item = adapter.getItem(position);
        shoppingList.select(item.getName());
    }

    @Override
    public void onItemUncheck(int position) {
        Item item = adapter.getItem(position);
        shoppingList.deselect(item.getName());
    }

    @Override
    public void onItemAdd(String inputText) {

        if (inputText.isEmpty() || shoppingList.contains(inputText)) {
            return;
        }

        Item item = new Item(inputText);
        shoppingList.add(item);
        shoppingList.select(item.getName());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3)
        {
            shoppingList.clearUnselected();
            adapter.notifyDataSetChanged();
        }
    }

    private void openConfirmActivity(){
        Intent intent = new Intent(getContext(), ConfirmActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(true));
        bundle.putParcelableArrayList("data", parcelableList);
        intent.putExtra("shoppingList", bundle);
        startActivityForResult(intent, 2);
    }
}
