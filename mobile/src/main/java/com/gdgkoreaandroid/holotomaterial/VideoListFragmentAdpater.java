package com.gdgkoreaandroid.holotomaterial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gdgkoreaandroid.holotomaterial.data.Category;

public class VideoListFragmentAdpater extends FragmentPagerAdapter {
    private final Category[] mCategories;

    public VideoListFragmentAdpater(FragmentManager fm, Category[] categories) {
        super(fm);
        mCategories = categories;
    }

    @Override
    public Fragment getItem(int index) {
        return VideoListFragment.newInstance(mCategories[index], true);
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
