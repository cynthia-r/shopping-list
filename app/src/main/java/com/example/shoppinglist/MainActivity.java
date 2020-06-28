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
        allItems = openFile(filename);

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private List<Item> openFile(String filename) {
        File directory = getFilesDir();
        File file = new File(directory, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (IOException e) {
                Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT);
                return new ArrayList<>();
            }
        }

        FileInputStream fis = null;
        try {
            fis = this.openFileInput(filename);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Could not find file", Toast.LENGTH_SHORT);
            return new ArrayList<>();
        }
        InputStreamReader inputStreamReader =
                new InputStreamReader(fis, StandardCharsets.UTF_8);

        List<Item> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                Item item = new Item(line);
                items.add(item);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to write to file", Toast.LENGTH_SHORT);
        }

        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void writeToFile(String filename, List<Item> items) {
        try (FileOutputStream fos = this.openFileOutput(filename, Context.MODE_PRIVATE)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for (Item item: items) {
                bw.write(item.getName());
                bw.newLine();
            }

        }
        catch (IOException e) {
            Toast.makeText(this, "Failed to write to file", Toast.LENGTH_SHORT);
        }
    }
}
