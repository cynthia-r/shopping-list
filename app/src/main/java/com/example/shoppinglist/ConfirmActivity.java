package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;

import com.example.shoppinglist.model.Item;

import java.util.ArrayList;

public class ConfirmActivity extends AppCompatActivity {

    private ConfirmListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle bundle = getIntent().getBundleExtra("shoppingList");
        ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("data");
        ArrayList<Item> itemList = new ArrayList<>();
        for (Parcelable parcelable : parcelableList) {
            itemList.add((Item)parcelable);
        }

        // set up the confirmed list RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfirmListViewAdapter(this, itemList);
        recyclerView.setAdapter(adapter);
    }
}
