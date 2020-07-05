package com.example.shoppinglist;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.example.shoppinglist.model.MarketItemComparator;
import com.example.shoppinglist.model.MarketItems;
import com.example.shoppinglist.model.PreviouslyBoughtItems;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.model.ShoppingListItem;
import com.example.shoppinglist.service.FileService;
import com.example.shoppinglist.service.SuggestedItemsService;

import java.util.ArrayList;

public class SuggestedItemsActivity extends AppCompatActivity implements ShoppingListViewAdapter.OnItemCheckListener,
        ShoppingListViewAdapter.OnLongClickListener, EditItemFragment.EditItemDialogListener {

    private ShoppingListViewAdapter recommendedListAdapter;
    private String currentList;
    private ShoppingList shoppingList;
    private ShoppingList suggestedItemList;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_items);

        // Retrieve the list
        Bundle bundle = getIntent().getBundleExtra(ListConstants.SHOPPING_LIST);
        ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList(ListConstants.DATA);

        FileService fileService = new FileService(this);

        MarketItems marketItems = fileService.readMarketItems(ListConstants.CATALOG);
        MarketItemComparator itemComparator = new MarketItemComparator(marketItems);
        shoppingList = new ShoppingList(itemComparator);

        for (Parcelable parcelable : parcelableList) {
            ShoppingListItem item = (ShoppingListItem)parcelable;
            shoppingList.add(item);
        }

        // Retrieve the list name
        currentList = bundle.getString(ListConstants.CURRENT_LIST);

        String previouslyBoughtFilename = fileService.getBoughtListFilename(currentList);
        PreviouslyBoughtItems previouslyBoughtItems = fileService.readPreviouslyBoughtItems(previouslyBoughtFilename);
        SuggestedItemsService suggestedItemsService = new SuggestedItemsService(previouslyBoughtItems);

        // Initialize a shopping list with all the recommended items unselected
        suggestedItemList = suggestedItemsService.getSuggestedItems(shoppingList);

        // Set up the suggested list RecyclerView
        RecyclerView recommendedRecyclerView = findViewById(R.id.recommended_list);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recommendedListAdapter = new ShoppingListViewAdapter(this, suggestedItemList);
        recommendedListAdapter.setItemCheckListener(this);
        recommendedListAdapter.setLongClickListener(this);
        recommendedRecyclerView.setAdapter(recommendedListAdapter);

        // Set up the Done button
        Button doneButton = findViewById(R.id.done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                openConfirmActivity();
            }
        });
    }

    @Override
    public void onItemCheck(int position) {
        ShoppingListItem item = recommendedListAdapter.getItem(position);
        suggestedItemList.select(item.getItemName());
    }

    @Override
    public void onItemUncheck(int position) {
        ShoppingListItem item = recommendedListAdapter.getItem(position);
        suggestedItemList.deselect(item.getItemName());
    }

    @Override
    public void onLongClick(ShoppingListItem item) {
        FragmentManager fm = getSupportFragmentManager();

        EditItemFragment alertDialog = EditItemFragment.newInstance(item.getItemName(), item.getQuantity());
        alertDialog.setEditItemDialogListener(this);
        alertDialog.show(fm, EditItemFragment.TAG);
    }

    @Override
    public void onItemEdit(String itemName, int quantity) {
        suggestedItemList.update(itemName, quantity);
        recommendedListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemDelete(String itemName) {
        suggestedItemList.remove(itemName);
        recommendedListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3) {
            // Finish the activity to go back to the main activity
            Intent intent = new Intent();
            setResult(3, intent);
            finish();
        }
    }

    private void openConfirmActivity(){
        // Add all the selected suggested items to the original shopping list
        shoppingList.addAll(suggestedItemList, true);

        Intent intent = new Intent(this, ConfirmActivity.class);

        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(false));
        bundle.putParcelableArrayList(ListConstants.DATA, parcelableList);
        bundle.putString(ListConstants.CURRENT_LIST, currentList);
        intent.putExtra(ListConstants.SHOPPING_LIST, bundle);

        startActivityForResult(intent, 2);
    }
}
