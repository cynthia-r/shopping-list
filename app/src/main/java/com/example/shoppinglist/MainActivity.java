package com.example.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoppingListViewAdapter.OnItemCheckListener, AddFragment.AddItemDialogListener {

    private ShoppingListViewAdapter adapter;
    private List<Item> allItems = new ArrayList<>();
    private List<Item> selectedItems = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String filename = "listFile";
        FileService fileService = new FileService(this);
        allItems = fileService.openFile(filename);

        String[] files = this.fileList();

        // data to populate the RecyclerView with
        /*ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(new Item("Barres"));
        itemList.add(new Item("Cereales cuicui"));
        itemList.add(new Item("Persil lapin"));
        itemList.add(new Item("Eau gallon"));
        itemList.add(new Item("Lardons"));*/

        // set up the shopping list RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListViewAdapter(this, allItems);
        adapter.setItemCheckListener(this);
        recyclerView.setAdapter(adapter);

        // Set up the Add button
        FloatingActionButton addButton = findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                AddFragment alertDialog = AddFragment.newInstance();
                alertDialog.show(fm, "fragment_alert");
            }
        });

        // Set up the Done button
        Button button = findViewById(R.id.done);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmActivity();
            }
        });
    }

    @Override
    public void onItemCheck(int position) {
        Item item = adapter.getItem(position);
        selectedItems.add(item);
    }

    @Override
    public void onItemUncheck(int position) {
        Item item = adapter.getItem(position);
        selectedItems.remove(item);
    }

    @Override
    public void onItemAdd(String inputText) {
        allItems.add(new Item(inputText));
        adapter.notifyDataSetChanged();
    }

    private void openConfirmActivity(){
        Intent intent = new Intent(this, ConfirmActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(selectedItems);
        bundle.putParcelableArrayList("data", parcelableList);
        intent.putExtra("shoppingList", bundle);
        startActivity(intent);
    }
}
