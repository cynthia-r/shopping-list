package com.example.shoppinglist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

        // Set up the Export button
        Button button = findViewById(R.id.export);
        button.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                sendEmail(itemList);
            }
        });

        // Set up the Done button
        Button doneButton = findViewById(R.id.save);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                save(itemList);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void save(List<Item> items) {
        String filename = "listFile";
        FileService fileService = new FileService(this);
        fileService.writeToFile(filename, items);

        Intent intent = new Intent();
        setResult(3, intent);
        finish();
    }

    private void sendEmail(List<Item> items) {

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(item.getName());
            sb.append("\n");
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"add-to-things-ryh544m1umg5aj3wf68@things.email"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Liste de courses");
        i.putExtra(Intent.EXTRA_TEXT   , sb.toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
