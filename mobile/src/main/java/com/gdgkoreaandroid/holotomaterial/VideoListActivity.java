package com.gdgkoreaandroid.holotomaterial;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gdgkoreaandroid.holotomaterial.data.Video;
import com.gdgkoreaandroid.holotomaterial.data.Videos;

/**
 * An activity representing a list of videos. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a MOVIE_LIST of items, which when touched,
 * lead to a {@link VideoDetailActivity} representing
 * item details. On tablets, the activity presents the MOVIE_LIST of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The MOVIE_LIST of items is a
 * {@link VideoListFragment} and the item details
 * (if present) is a {@link VideoDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link VideoListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class VideoListActivity extends ActionBarActivity
        implements VideoListFragment.Callbacks {

    private static final String TAG_FRAG_MY_VIDEO = "TAG_FRAG_MY_VIDEO";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private DrawerLayout mDrawerLayout;
    private View mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        initFragmentPager();
        initNaviDrawer();

    }

    private void initFragmentPager() {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        VideoListFragmentAdpater adpater = new VideoListFragmentAdpater(
                getSupportFragmentManager(), Videos.getCategoryArray());
        pager.setAdapter(adpater);
    }

    private void initNaviDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.left_drawer);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

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
                    case R.id.left_drawer_settings:
                        showSettings();
                        break;
                    case R.id.left_drawer_help:
                        showHelp();
                        break;
                }
            }
        };

        View allVideos = mDrawerList.findViewById(R.id.left_drawer_all_videos);
        View myVideos = mDrawerList.findViewById(R.id.left_drawer_my_videos);
        View settings = mDrawerList.findViewById(R.id.left_drawer_settings);
        View help = mDrawerList.findViewById(R.id.left_drawer_help);

        allVideos.setOnClickListener(clickListener);
        allVideos.setSelected(true);
        myVideos.setOnClickListener(clickListener);
        settings.setOnClickListener(clickListener);
        help.setOnClickListener(clickListener);
    }

    private void showHelp() {
        Toast.makeText(this, "Show Help", Toast.LENGTH_SHORT).show();
    }

    private void showSettings() {
        Toast.makeText(this, "Show Settings", Toast.LENGTH_SHORT).show();
    }

    private void showMyVideos() {
        final VideoListFragment fragment
                = VideoListFragment.newInstance(Videos.getCategoryArray()[0], false);

        final FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.content_container, fragment, TAG_FRAG_MY_VIDEO);
        transaction.commit();

        View myVideos = mDrawerList.findViewById(R.id.left_drawer_my_videos);
        myVideos.setSelected(true);

        mDrawerLayout.closeDrawers();
    }

    private void showAllVideos() {

        final FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentByTag(TAG_FRAG_MY_VIDEO);
        if (frag != null) {
            FragmentTransaction tr = fm.beginTransaction();
            tr.remove(frag);
            tr.commit();
        }

        View allVideos = mDrawerList.findViewById(R.id.left_drawer_all_videos);
        allVideos.setSelected(true);

        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback method from {@link VideoListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(Video video) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            //arguments.putLong(Videos.ARG_ITEM_ID, id);
            VideoDetailFragment fragment = new VideoDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, VideoDetailActivity.class);
            detailIntent.putExtra(VideoDetailActivity.KEY_BUNDLE_VIDEO, video);
            startActivity(detailIntent);
        }
    }
}
