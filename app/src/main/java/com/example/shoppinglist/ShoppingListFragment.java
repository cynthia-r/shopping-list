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
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.example.shoppinglist.service.FileService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements /*ShoppingListViewAdapter.OnItemCheckListener,*/
        /*AddFragment.AddItemDialogListener,*/ AdapterView.OnItemSelectedListener, /*EditItemFragment.EditItemDialogListener,*/ ViewPager.OnPageChangeListener {

    public static final String TAG = "ShoppingList";

    //private ShoppingListViewAdapter adapter;
    //private RecyclerView recyclerView;
    //private ShoppingList shoppingList;
    private String currentList;
    private List<String> lists;

    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    public void setCurrentList(String list) {
        currentList = list;
    }

    public static ShoppingListFragment newInstance(String list) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        fragment.setCurrentList(list);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        // Instantiate a ViewPager and a PagerAdapter
        mPager = rootView.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.lists_array));
        mPager.setAdapter(pagerAdapter);
        mPager.setOnPageChangeListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();

        MainActivity activity = (MainActivity)getActivity();

        FileService fileService = new FileService(activity);

        lists = Arrays.asList(getResources().getStringArray(R.array.lists_array));

        /*if (currentList == null || currentList.isEmpty()) {
            currentList = fileService.getCurrentList(ListConstants.CURRENT_LIST);
        }*/
        if (currentList == null || currentList.isEmpty()) {
            currentList = lists.get(0);
        }

        /*String filename = fileService.getShoppingListFilename(currentList);
        shoppingList = fileService.readShoppingList(filename);*/

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

    /*@RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
    }*/

    /*@Override
    public void onItemAdd(String itemName, int quantity) {

        if (itemName.isEmpty() || shoppingList.contains(itemName)) {
            return;
        }

        Item item = new Item(itemName);
        shoppingList.add(item, quantity, true);

        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(shoppingList.getPosition(itemName));
    }*/

    /*@Override
    public void onItemEdit(String itemName, int quantity) {
        shoppingList.update(itemName, quantity);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(String itemName) {
        shoppingList.remove(itemName);
        adapter.notifyDataSetChanged();
    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3) {
            shoppingList.clearUnselected();
            adapter.notifyDataSetChanged();
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        /*String listName = (String) adapterView.getItemAtPosition(i);

        if (listName.equals(currentList)) {
            return;
        }

        // Save current list
        FileService fileService = new FileService(getContext());
        String filename = fileService.getShoppingListFilename(currentList);
        fileService.saveShoppingList(filename, shoppingList);

        // Switch to the other list
        currentList = listName;

        filename = fileService.getShoppingListFilename(currentList);
        ShoppingList newShoppingList = fileService.readShoppingList(filename);

        shoppingList.clear();
        shoppingList.addAll(newShoppingList, false);

        adapter.notifyDataSetChanged();*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // No-op
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openSuggestedItemsActivity(){
        FileService fileService = new FileService(getContext());
        String filename = fileService.getShoppingListFilename(currentList);
        ShoppingList shoppingList = fileService.readShoppingList(filename);

        Intent intent = new Intent(getContext(), SuggestedItemsActivity.class);

        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(false));
        bundle.putParcelableArrayList(ListConstants.DATA, parcelableList);
        bundle.putString(ListConstants.CURRENT_LIST, currentList);
        intent.putExtra(ListConstants.SHOPPING_LIST, bundle);

        startActivityForResult(intent, 2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // No-op
    }

    @Override
    public void onPageSelected(int position) {
        currentList = lists.get(position);

        // TODO update spinner
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // No-op
    }

    public String getCurrentList() {
        return currentList;
    }
}
