package com.example.shoppinglist;

import android.widget.CursorAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    private String[] mLists;

    public ScreenSlidePagerAdapter(FragmentManager fm, String[] lists) {
        super(fm);
        mLists = lists;
    }

    @Override
    public Fragment getItem(int position) {
        String currentList = mLists[position];
        return CurrentListFragment.newInstance(currentList);
    }

    @Override
    public int getCount() {
        return mLists.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mLists[position];
    }
}
