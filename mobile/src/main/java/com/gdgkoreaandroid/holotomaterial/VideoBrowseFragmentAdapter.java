package com.gdgkoreaandroid.holotomaterial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gdgkoreaandroid.holotomaterial.data.Category;

public class VideoBrowseFragmentAdapter extends FragmentPagerAdapter {
    private final Category[] mCategories;

    public VideoBrowseFragmentAdapter(
            FragmentManager fm, Category[] categories) {
        super(fm);
        mCategories = categories;
    }

    @Override
    public Fragment getItem(int index) {
        return VideoBrowseFragment.newInstance(mCategories[index]);
    }

    @Override
    public int getCount() {
        return mCategories.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories[position].category;
    }
}
