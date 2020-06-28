package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shoppinglist.model.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoppingListViewAdapter.OnItemCheckListener {

    private ShoppingListViewAdapter adapter;
    private List<Item> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // data to populate the RecyclerView with
        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Barres"));
        itemList.add(new Item("Cereales cuicui"));
        itemList.add(new Item("Persil lapin"));
        itemList.add(new Item("Eau gallon"));
        itemList.add(new Item("Lardons"));

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListViewAdapter(this, itemList);
        adapter.setItemCheckListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemCheck(int position) {
        Item item = adapter.getItem(position);
        //Toast.makeText(this, "You checked " + item.getName(), Toast.LENGTH_SHORT).show();
        selectedItems.add(item);
    }

    @Override
    public void onItemUncheck(int position) {
        Item item = adapter.getItem(position);
        //Toast.makeText(this, "You unchecked " + item.getName(), Toast.LENGTH_SHORT).show();
        selectedItems.remove(item);
    }
}
