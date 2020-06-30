package com.example.shoppinglist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShoppingListViewAdapter.OnItemCheckListener, AddFragment.AddItemDialogListener {

    private ActionBarDrawerToggle drawerToggle;
    private ShoppingListViewAdapter adapter;
    private ShoppingList shoppingList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_shopping_list:
                        Toast.makeText(MainActivity.this, "Shopping list",Toast.LENGTH_SHORT).show();break;
                    case R.id.nav_catalog:
                        Toast.makeText(MainActivity.this, "Catalog",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }
        });

        String filename = "listFile";
        FileService fileService = new FileService(this);
        shoppingList = fileService.openFile(filename);

        // set up the shopping list RecyclerView
        RecyclerView recyclerView = findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ShoppingListViewAdapter(this, shoppingList);
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
        Button button = findViewById(R.id.save);
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
        FileService fileService = new FileService(this);
        fileService.writeToFile(filename, shoppingList);

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
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

    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3)
        {
            FileService fileService = new FileService(this);
            //shoppingList = fileService.openFile("listFile");
            shoppingList.clearUnselected();
            adapter.notifyDataSetChanged();
        }
    }

    private void openConfirmActivity(){
        Intent intent = new Intent(this, ConfirmActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(true));
        bundle.putParcelableArrayList("data", parcelableList);
        intent.putExtra("shoppingList", bundle);
        startActivityForResult(intent, 2);
    }
}
