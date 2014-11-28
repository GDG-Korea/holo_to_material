package com.gdgkoreaandroid.holotomaterial;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.common.view.SlidingTabLayout;
import com.gdgkoreaandroid.holotomaterial.data.Video;
import com.gdgkoreaandroid.holotomaterial.data.Videos;

//import android.support.v7.app.ActionBarDrawerToggle;

/**
 * An activity representing a list of videos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a MOVIE_LIST of items, which when touched,
 * lead to a {@link VideoDetailActivity} representing
 * item details. On tablets, the activity presents the MOVIE_LIST of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The MOVIE_LIST of items is a
 * {@link VideoBrowseFragment} and the item details
 * (if present) is a {@link VideoDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link VideoBrowseFragment.Callbacks} interface
 * to listen for item selections.
 */
public class VideoBrowseActivity extends ActionBarActivity
        implements VideoBrowseFragment.Callbacks {

    private static final String TAG = "VideoBrowseActivity";

    private View  mToolbar;
    private View mTabContainer;

    private int mMaxTabContainerY;
    private int mMinTabContainerY;
    private int mMaxAppBarY;
    private int mMinAppBarY;
    private int mActionbarHeight;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView.OnScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_browse);

        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            mActionbarHeight
                    = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        initToolbar();
        initFragmentPager();
        initNaviDrawer();
        initParallaxEffect();
    }

    private void initToolbar() {

        Log.d(TAG, "initToolbar");
        //mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(mToolbar);
    }

    private void initFragmentPager() {

        Log.d(TAG, "initFragmentPager");

        final ViewPager pager = (ViewPager) findViewById(R.id.content_pager);
        final VideoBrowseFragmentAdapter adpater = new VideoBrowseFragmentAdapter(
                getSupportFragmentManager(), Videos.getCategoryArray());
        pager.setAdapter(adpater);

        final SlidingTabLayout tabs = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        tabs.setDividerColors(getResources().getColor(R.color.colorPirmary));
        tabs.setSelectedIndicatorColors(getResources().getColor(R.color.white_alpha_87));
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            public static final String TAG = "OnPageChangeListener";

            @Override
            public void onPageScrolled(int index, float v, int i2) {
                // IGNORED
            }

            @Override
            public void onPageSelected(int index) {
                mToolbar.animate().y(mMaxAppBarY).start();
                mTabContainer.animate().y(mMaxTabContainerY).start();
            }

            @Override
            public void onPageScrollStateChanged(int index) {
                // IGNORED
            }
        });

        mTabContainer = findViewById(R.id.tab_container);
        //tabs.setViewPager(pager);
    }

    private void initNaviDrawer() {

        Log.d(TAG, "initNaviDrawer");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final View drawerList = findViewById(R.id.left_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        // Handle Navigation Click Events
        View.OnClickListener clickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.left_drawer_all_videos:
                        showAllVideos();
                        break;
                    case R.id.left_drawer_my_videos:
                        showMyVideos();
                        break;
                    case R.id.left_drawer_purchase_videos:
                        launchPlayMovie();
                        break;
                    case R.id.left_drawer_settings:
                        showSettings();
                        break;
                    case R.id.left_drawer_help:
                        showHelp();
                        break;
                }
            }
        };

        View allVideos = drawerList.findViewById(R.id.left_drawer_all_videos);
        View myVideos = drawerList.findViewById(R.id.left_drawer_my_videos);
        View shopping = drawerList.findViewById(R.id.left_drawer_purchase_videos);
        View settings = drawerList.findViewById(R.id.left_drawer_settings);
        View help = drawerList.findViewById(R.id.left_drawer_help);

        allVideos.setOnClickListener(clickListener);
        myVideos.setOnClickListener(clickListener);
        shopping.setOnClickListener(clickListener);
        settings.setOnClickListener(clickListener);
        help.setOnClickListener(clickListener);
    }

    private void initParallaxEffect() {

        mMaxTabContainerY = mActionbarHeight;
        mMinTabContainerY = 0;

        mMinAppBarY = -mActionbarHeight;
        mMaxAppBarY = 0;

        mScrollListener = new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // IGNORED
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // TODO
            }
        };
    }

    private void showAllVideos() {
        mDrawerLayout.closeDrawers();
    }

    private void showMyVideos() {
        Toast.makeText(this, "Show My Videos", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();
    }

    private void launchPlayMovie() {
        Toast.makeText(this, "Launch Play Movie", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();
    }

    private void showSettings() {
        Toast.makeText(this, "Show Settings", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();
    }

    private void showHelp() {
        Toast.makeText(this, "Show Help", Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link VideoBrowseFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(View view, Video video) {

        // In single-pane mode, simply start the detail activity
        // for the selected item ID.
        Intent detailIntent = new Intent(this, VideoDetailActivity.class);
        detailIntent.putExtra(VideoDetailActivity.KEY_BUNDLE_VIDEO, video);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // start the new activity
            startActivityWithTransition(detailIntent, view);
        } else {
            startActivity(detailIntent);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startActivityWithTransition(Intent detailIntent, View view) {
        startActivity(detailIntent);
    }

    @Override
    public RecyclerView.OnScrollListener getScrollListener() {
        return mScrollListener;
    }
}
