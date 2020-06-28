package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shoppinglist.model.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements /*ShoppingListViewAdapter.ItemClickListener,*/ ShoppingListViewAdapter.OnItemCheckListener {

    ShoppingListViewAdapter adapter;

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
        //adapter.setClickListener(this);
        adapter.setItemCheckListener(this);
        recyclerView.setAdapter(adapter);
    }

    // TODO see if I need this functionality
    /*@Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position).getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onItemCheck(int position) {
        Item item = adapter.getItem(position);
        Toast.makeText(this, "You checked " + item.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemUncheck(int position) {
        Item item = adapter.getItem(position);
        Toast.makeText(this, "You unchecked " + item.getName(), Toast.LENGTH_SHORT).show();
    }
}
