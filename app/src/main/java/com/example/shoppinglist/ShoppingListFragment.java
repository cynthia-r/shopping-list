package com.example.shoppinglist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

import com.example.shoppinglist.model.PreviouslyBoughtItems;
import com.example.shoppinglist.model.ShoppingList;
import com.example.shoppinglist.service.FileService;
import com.example.shoppinglist.service.SuggestedItemsService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {

    public static final String TAG = "ShoppingList";

    private String currentList;
    private List<String> lists;

    private Spinner spinner;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

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

        currentList = fileService.getCurrentList(ListConstants.CURRENT_LIST);
        if (currentList == null || currentList.isEmpty()) {
            currentList = lists.get(0);
        }

        // Setup the spinner
        spinner = activity.findViewById(R.id.list_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.lists_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        int currentListIndex = lists.indexOf(currentList);
        spinner.setSelection(currentListIndex);
        mPager.setCurrentItem(currentListIndex);

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
        FileService fileService = new FileService(getContext());
        fileService.saveCurrentList(ListConstants.CURRENT_LIST, currentList);

        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == 3) {

            CurrentListFragment currentListFragment = getCurrentListFragment();
            currentListFragment.onConfirmActivityResult();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String listName = (String) adapterView.getItemAtPosition(i);

        if (listName.equals(currentList)) {
            return;
        }

        mPager.setCurrentItem(i, true);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // No-op
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // No-op
    }

    @Override
    public void onPageSelected(int position) {
        currentList = lists.get(position);

        spinner.setSelection(lists.indexOf(currentList));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        // No-op
    }

    public CurrentListFragment getCurrentListFragment() {
        // Find which fragment is currently being displayed
        FragmentManager cfm = getChildFragmentManager();

        List<Fragment> fragmentList = cfm.getFragments();
        Fragment currentFragment = null;
        int i=0;
        while (i<fragmentList.size()) {
            CurrentListFragment currentListFragment = (CurrentListFragment) fragmentList.get(i);
            if (currentListFragment.getCurrentList().equals(currentList)) {
                currentFragment = currentListFragment;
                break;
            }
            i++;
        }
        return (CurrentListFragment) currentFragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void openSuggestedItemsActivity(){

        ShoppingList shoppingList = getCurrentListFragment().getShoppingList();

        FileService fileService = new FileService(getContext());

        // Check if there are any recommended items
        String previouslyBoughtFilename = fileService.getBoughtListFilename(currentList);
        PreviouslyBoughtItems previouslyBoughtItems = fileService.readPreviouslyBoughtItems(previouslyBoughtFilename);
        SuggestedItemsService suggestedItemsService = new SuggestedItemsService(previouslyBoughtItems);

        // Initialize a shopping list with all the recommended items unselected
        ShoppingList suggestedItemList = suggestedItemsService.getSuggestedItems(shoppingList);

        // Go to the confirm activity directly if there are no recommended items
        if (suggestedItemList.size() == 0) {
            openConfirmActivity(shoppingList);
        }
        // Otherwise go to the suggested items activity
        else {
            Intent intent = new Intent(getContext(), SuggestedItemsActivity.class);

            Bundle bundle = new Bundle();
            ArrayList<Parcelable> parcelableList = new ArrayList<>();
            parcelableList.addAll(shoppingList.toList(false));
            bundle.putParcelableArrayList(ListConstants.SHOPPING_LIST_DATA, parcelableList);

            ArrayList<Parcelable> suggestedItemParcelableList = new ArrayList<>();
            suggestedItemParcelableList.addAll(suggestedItemList.toList(false));
            bundle.putParcelableArrayList(ListConstants.SUGGESTED_LIST_DATA, suggestedItemParcelableList);

            bundle.putString(ListConstants.CURRENT_LIST, currentList);
            intent.putExtra(ListConstants.SHOPPING_LIST, bundle);

            startActivityForResult(intent, 2);
        }
    }

    private void openConfirmActivity(ShoppingList shoppingList){
        Intent intent = new Intent(getContext(), ConfirmActivity.class);

        Bundle bundle = new Bundle();
        ArrayList<Parcelable> parcelableList = new ArrayList<>();
        parcelableList.addAll(shoppingList.toList(false));
        bundle.putParcelableArrayList(ListConstants.SHOPPING_LIST_DATA, parcelableList);
        bundle.putString(ListConstants.CURRENT_LIST, currentList);
        intent.putExtra(ListConstants.SHOPPING_LIST, bundle);

        startActivityForResult(intent, 2);
    }
}
