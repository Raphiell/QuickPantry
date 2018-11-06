package com.example.quickpantry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    int numberOfTabs;

    /**
     * Constructor
     * @param manager       Fragment Manager
     * @param numberOfTabs  Number of tabs in layout
     */
    public MyPagerAdapter(FragmentManager manager, int numberOfTabs)
    {
        super(manager);
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new PantryFragment();
            case 1:
                return new RecipeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}