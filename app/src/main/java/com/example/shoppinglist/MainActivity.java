package com.example.shoppinglist;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.shoppinglist.model.Item;
import com.example.shoppinglist.model.ShoppingListItem;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ShoppingListViewAdapter.OnLongClickListener, CatalogListViewAdapter.OnLongClickListener {

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private int currentNavItemId;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.activity_main);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                displayFragment(id);

                navigationView.getMenu().findItem(currentNavItemId).setChecked(false);

                // Highlight the selected item has been done by NavigationView
                menuItem.setChecked(true);
                currentNavItemId = id;

                // Set action bar title
                setTitle(menuItem.getTitle());

                return true;
            }
        });

        currentNavItemId = R.id.nav_shopping_list;
        displayFragment(currentNavItemId);
        navigationView.getMenu().findItem(currentNavItemId).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(drawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void displayFragment(int id) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass;
        String tag;
        switch(id)
        {
            case R.id.nav_shopping_list:
                fragmentClass = ShoppingListFragment.class;
                tag = ShoppingListFragment.TAG;
                break;
            case R.id.nav_catalog:
                fragmentClass = CatalogFragment.class;
                tag = CatalogFragment.TAG;
                break;
            default:
                fragmentClass = ShoppingListFragment.class;
                tag = ShoppingListFragment.TAG;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.replace(R.id.fragment_frame, fragment, tag);
            fragmentTransaction.addToBackStack(null);

            fragmentTransaction.commit();
        }

        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    // TODO find a less convoluted solution to this and same for the one below
    @Override
    public void onClick(View view) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_frame);
        String tag = fragment.getTag();
        if (tag == ShoppingListFragment.TAG) {
            ShoppingListFragment shoppingListFragment = (ShoppingListFragment)fragment;
            AddFragment alertDialog = AddFragment.newInstance();
            alertDialog.setAddItemDialogListener(shoppingListFragment);
            alertDialog.show(fm, AddFragment.TAG);
        }
        else if (tag == CatalogFragment.TAG) {
            CatalogFragment catalogFragment = (CatalogFragment)fragment;
            AddCatalogItemFragment alertDialog = AddCatalogItemFragment.newInstance();
            alertDialog.setAddCatalogItemDialogListener(catalogFragment);
            alertDialog.show(fm, AddCatalogItemFragment.TAG);
        }
    }

    @Override
    public void onLongClick(ShoppingListItem item) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_frame);

        ShoppingListFragment shoppingListFragment = (ShoppingListFragment)fragment;
        EditItemFragment alertDialog = EditItemFragment.newInstance(item.getItemName(), item.getQuantity());
        alertDialog.setEditItemDialogListener(shoppingListFragment);
        alertDialog.show(fm, EditItemFragment.TAG);
    }

    @Override
    public void onLongClick(Item item, int position) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.fragment_frame);

        CatalogFragment catalogFragment = (CatalogFragment)fragment;
        EditCatalogFragment alertDialog = EditCatalogFragment.newInstance(item.getName(), position);
        alertDialog.setEditCatalogItemDialogListener(catalogFragment);
        alertDialog.show(fm, EditCatalogFragment.TAG);
    }
}
