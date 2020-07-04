package com.example.shoppinglist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements ShoppingListViewAdapter.OnItemCheckListener,
        AddFragment.AddItemDialogListener, AdapterView.OnItemSelectedListener, EditItemFragment.EditItemDialogListener {

    private ShoppingListViewAdapter adapter;
    private RecyclerView recyclerView;
    private ShoppingList shoppingList;
    private String currentList;

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

        FileService fileService = new FileService(activity);

        List<String> lists = Arrays.asList(getResources().getStringArray(R.array.lists_array));

        currentList = fileService.getCurrentList("currentList");
        if (currentList.isEmpty()) {
            currentList = lists.get(0);
        }

        String filename = currentList + "-listFile";
        shoppingList = fileService.readShoppingList(filename);

        // set up the shopping list RecyclerView
        recyclerView = activity.findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShoppingListViewAdapter(activity, shoppingList);
        adapter.setItemCheckListener(this);
        adapter.setLongClickListener(activity);
        recyclerView.setAdapter(adapter);

        // Setup the spinner
        Spinner spinner = activity.findViewById(R.id.list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.lists_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(lists.indexOf(currentList));

        // Set up the Add button
        FloatingActionButton addButton = activity.findViewById(R.id.add);
        addButton.setOnClickListener(activity);

        // Set up the Done button
        Button button = activity.findViewById(R.id.save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuggestedItemsActivity();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop () {
        String filename = currentList + "-listFile";
        FileService fileService = new FileService(getContext());
        fileService.saveShoppingList(filename, shoppingList);

        fileService.saveCurrentList("currentList", currentList);

        super.onStop();
    }

    @Override
    public void onItemCheck(int position) {
        ShoppingListItem item = adapter.getItem(position);
        shoppingList.select(item.getItemName());
    }

    @Override
    public void onItemUncheck(int position) {
        ShoppingListItem item = adapter.getItem(position);
        shoppingList.deselect(item.getItemName());
    }

    @Override
    public void onItemAdd(String itemName, int quantity) {

        if (itemName.isEmpty() || shoppingList.contains(itemName)) {
            return;
        }

        Item item = new Item(itemName);
        shoppingList.add(item, quantity, true);

        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(shoppingList.getPosition(itemName));
    }

    @Override
    public void onItemEdit(String itemName, int quantity) {
        shoppingList.update(itemName, quantity);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(String itemName) {
        shoppingList.remove(itemName);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3) {
            shoppingList.clearUnselected();
            adapter.notifyDataSetChanged();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String listName = (String) adapterView.getItemAtPosition(i);

        if (listName.equals(currentList)) {
            return;
        }

        // Save current list
        FileService fileService = new FileService(getContext());
        String filename = currentList + "-listFile";
        fileService.saveShoppingList(filename, shoppingList);

        // Switch to the other list
        currentList = listName;

        filename = currentList + "-listFile";
        ShoppingList newShoppingList = fileService.readShoppingList(filename);

        shoppingList.clear();
        shoppingList.addAll(newShoppingList);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // No-op
    }

    private void openSuggestedItemsActivity(){
        Intent intent = new Intent(getContext(), SuggestedItemsActivity.class);

        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(true));
        bundle.putParcelableArrayList("data", parcelableList);
        bundle.putString("currentList", currentList);
        intent.putExtra("shoppingList", bundle);

        startActivityForResult(intent, 2);
    }
}
