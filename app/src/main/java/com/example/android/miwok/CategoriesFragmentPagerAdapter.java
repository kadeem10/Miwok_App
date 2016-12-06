package com.example.android.miwok;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Reshaud Ally on 12/6/2016.
 */

public class CategoriesFragmentPagerAdapter extends FragmentPagerAdapter {
    public CategoriesFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //Switch Fragments as needed
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NumbersFragment();
            case 1:
                return new FamilyFragment();
            case 2:
                return new ColorsFragment();
            default:
                return new PhrasesFragment();
        }
    }

    //Total number of fragments
    @Override
    public int getCount() {
        return 4;
    }
}
