package com.example.shoppinglist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.shoppinglist.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ConfirmActivity extends AppCompatActivity {

    private ConfirmListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle bundle = getIntent().getBundleExtra("shoppingList");
        ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("data");
        final List<Item> itemList = new ArrayList<>();
        for (Parcelable parcelable : parcelableList) {
            itemList.add((Item)parcelable);
        }

        // set up the confirmed list RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConfirmListViewAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        // Set up the Done button
        Button button = findViewById(R.id.go);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                saveAndGo(itemList);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveAndGo(List<Item> items) {
        String filename = "listFile";
        FileService fileService = new FileService(this);
        fileService.writeToFile(filename, items);

        //NavUtils.navigateUpFromSameTask(this);
        finish();
    }
}
