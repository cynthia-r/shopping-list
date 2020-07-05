package com.example.shoppinglist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.MarketItemComparator;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;
import com.example.shoppinglist.service.FileService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentListFragment extends Fragment implements ShoppingListViewAdapter.OnItemCheckListener,
        AddFragment.AddItemDialogListener, EditItemFragment.EditItemDialogListener {

    public static final String TAG = "CurrentList";

    private String currentList;
    private ShoppingList shoppingList;

    private RecyclerView recyclerView;
    private ShoppingListViewAdapter adapter;

    public CurrentListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment CurrentListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CurrentListFragment newInstance(String param1) {
        CurrentListFragment fragment = new CurrentListFragment();
        fragment.setCurrentList(param1);
        return fragment;
    }

    public String getCurrentList() {
        return currentList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_current_list, container, false);

        //TextView textView = rootView.findViewById(R.id.text1);
        //textView.setText(currentList);

        MainActivity activity = (MainActivity)getActivity();

        FileService fileService = new FileService(activity);

        List<String> lists = Arrays.asList(getResources().getStringArray(R.array.lists_array));

        if (currentList == null || currentList.isEmpty()) {
            currentList = fileService.getCurrentList(ListConstants.CURRENT_LIST);
        }
        if (currentList.isEmpty()) {
            currentList = lists.get(0);
        }

        String filename = fileService.getShoppingListFilename(currentList);
        shoppingList = fileService.readShoppingList(filename);

        // set up the shopping list RecyclerView
        ShoppingList fooList = new ShoppingList(new MarketItemComparator(new MarketItems()));
        fooList.add(new ShoppingListItem(new Item(currentList), 1, false));
        recyclerView = rootView.findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShoppingListViewAdapter(activity, shoppingList);
        //adapter = new ShoppingListViewAdapter(activity, fooList);
        adapter.setItemCheckListener(this);
        adapter.setLongClickListener(activity);
        recyclerView.setAdapter(adapter);

        /*// Set up the Add button
        FloatingActionButton addButton = rootView.findViewById(R.id.add);
        addButton.setOnClickListener(activity);*/

        // Inflate the layout for this fragment
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        //MainActivity activity = (MainActivity)getActivity();

        /*FileService fileService = new FileService(activity);

        List<String> lists = Arrays.asList(getResources().getStringArray(R.array.lists_array));

        if (currentList == null || currentList.isEmpty()) {
            currentList = fileService.getCurrentList(ListConstants.CURRENT_LIST);
        }
        if (currentList.isEmpty()) {
            currentList = lists.get(0);
        }

        String filename = fileService.getShoppingListFilename(currentList);
        shoppingList = fileService.readShoppingList(filename);

        // set up the shopping list RecyclerView
        recyclerView = activity.findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShoppingListViewAdapter(activity, shoppingList);
        adapter.setItemCheckListener(this);
        adapter.setLongClickListener(activity);
        recyclerView.setAdapter(adapter);*/


    }

    public void setCurrentList(String list) {
        currentList = list;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStop () {
        FileService fileService = new FileService(getContext());
        String filename = fileService.getShoppingListFilename(currentList);
        fileService.saveShoppingList(filename, shoppingList);

        fileService.saveCurrentList(ListConstants.CURRENT_LIST, currentList);

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

        // TODO fix this - list doesn't get updated after save

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3) {
            shoppingList.clearUnselected();
            adapter.notifyDataSetChanged();
        }
    }
}
