package com.example.shoppinglist;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;
import com.example.shoppinglist.service.FileService;

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
     * @param list list.
     * @return A new instance of fragment CurrentListFragment.
     */
    public static CurrentListFragment newInstance(String list) {
        CurrentListFragment fragment = new CurrentListFragment();
        fragment.setCurrentList(list);
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

        MainActivity activity = (MainActivity)getActivity();

        FileService fileService = new FileService(activity);

        String filename = fileService.getShoppingListFilename(currentList);
        shoppingList = fileService.readShoppingList(filename);

        // Set up the shopping list RecyclerView
        recyclerView = rootView.findViewById(R.id.main_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ShoppingListViewAdapter(activity, shoppingList);
        adapter.setItemCheckListener(this);
        adapter.setLongClickListener(activity);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onConfirmActivityResult()
    {
        // Update the shopping list from file
        FileService fileService = new FileService(getContext());
        String filename = fileService.getShoppingListFilename(currentList);
        ShoppingList newShoppingList = fileService.readShoppingList(filename);

        shoppingList.clear();
        shoppingList.addAll(newShoppingList, false);
        adapter.notifyDataSetChanged();
    }
}
