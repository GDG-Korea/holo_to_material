package com.gdgkoreaandroid.holotomaterial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.gdgkoreaandroid.holotomaterial.data.Video;


/**
 * An activity representing a single Video detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a MOVIE_LIST of items
 * in a {@link VideoListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link VideoDetailFragment}.
 */
public class VideoDetailActivity extends ActionBarActivity {

    public static final String KEY_BUNDLE_VIDEO = "key_bundle_video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Video video = getIntent().getExtras().getParcelable(KEY_BUNDLE_VIDEO);
            VideoDetailFragment fragment = VideoDetailFragment.newInstance(video);

            getFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, VideoListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
