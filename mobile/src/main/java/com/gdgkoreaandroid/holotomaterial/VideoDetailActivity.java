package com.gdgkoreaandroid.holotomaterial;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgkoreaandroid.holotomaterial.data.Video;
import com.google.samples.apps.iosched.ui.widget.ObservableScrollView;


/**
 * An activity representing a single Video detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a MOVIE_LIST of items
 * in a {@link VideoBrowseActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link VideoDetailFragment}.
 */
public class VideoDetailActivity extends ActionBarActivity {

    public static final String KEY_BUNDLE_VIDEO = "key_bundle_video";

    private Video mSelectedVideo;

    private float mInitialContentHeaderPos;
    private float mMaxContentImagePos;
    private boolean mIsHeaderSticky;
    private ObservableScrollView mObservableScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);

        mSelectedVideo = getIntent().getExtras().getParcelable(KEY_BUNDLE_VIDEO);
        mObservableScrollView = (ObservableScrollView) findViewById(R.id.activity_detail_scroller);

        loadImage();
        initToolbar();
        initContent(savedInstanceState);
        initContentHeader();
        initFAB();
        initParallaxEffect();
    }

    private void loadImage() {

        final ImageView iv = (ImageView) findViewById(R.id.content_image);

        if (mSelectedVideo != null) {
            Glide.with(this).fromUri()
                    .centerCrop()
                    .placeholder(
                            new ColorDrawable(getResources().getColor(R.color.colorPrimaryLight)))
                    .crossFade(300)
                    .load(mSelectedVideo.getBackgroundUri())
                    .thumbnail(0.1f)
                    .into(iv);
        }
    }

    private void initToolbar() {
        // Show the Up button in the action bar.
        final Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    private void initContent(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            VideoDetailFragment fragment = VideoDetailFragment.newInstance(mSelectedVideo);
            getFragmentManager().beginTransaction()
                    .add(R.id.content_container, fragment)
                    .commit();
        }
    }

    private void initContentHeader() {
        TextView title = (TextView) findViewById(R.id.content_title);
        TextView meta = (TextView) findViewById(R.id.content_studio);

        title.setText(mSelectedVideo.title);
        meta.setText(mSelectedVideo.studio);

        final View contentHeader = mObservableScrollView.findViewById(R.id.content_header);
        contentHeader.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mInitialContentHeaderPos = contentHeader.getY();
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            contentHeader.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });

        final View contentImage = mObservableScrollView.findViewById(R.id.content_image);
        contentHeader.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mMaxContentImagePos = contentImage.getHeight();

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            contentImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
    }

    private void initFAB() {

        final View view = findViewById(R.id.content_play);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PlayerActivity.class);
                intent.putExtra(PlayerActivity.BUNDLE_KEY_VIDEO, mSelectedVideo);
                intent.putExtra(v.getContext().getString(R.string.should_start), true);
                v.getContext().startActivity(intent);
            }
        });

        view.setVisibility(View.INVISIBLE);
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }

                        view.setY(view.getY() + getResources().getDimensionPixelSize(
                                R.dimen.fab_margin_with_appbar));
                        view.setScaleX(0.1f);
                        view.setScaleY(0.1f);
                        view.setAlpha(0.f);
                        view.setVisibility(View.VISIBLE);
                        view.animate().scaleX(1.f).scaleY(1.f).alpha(1.f)
                                .setDuration(300)
                                .setInterpolator(new OvershootInterpolator()).setStartDelay(500).start();
                    }
                });
    }

    private void initParallaxEffect() {

        final View contentImage = mObservableScrollView.findViewById(R.id.content_image);
        final View contentHeader = mObservableScrollView.findViewById(R.id.content_header);
        final View contentHeaderShadow
                = mObservableScrollView.findViewById(R.id.content_header_shadow);
        final View fabPlayButton = mObservableScrollView.findViewById(R.id.content_play);

        mObservableScrollView.addCallbacks(new ObservableScrollView.Callbacks() {
            public static final String TAG = "ObservableScrollView";

            @Override
            public void onScrollChanged(int deltaX, int deltaY) {
                final int scrollY = mObservableScrollView.getScrollY();

                // relocate the position of contentHeader
                if (contentHeader.getY() < scrollY) {
                    mIsHeaderSticky = true;
                } else if (scrollY <= mInitialContentHeaderPos) {
                    mIsHeaderSticky = false;
                    contentHeader.setY(mInitialContentHeaderPos);
                    contentHeaderShadow.setY(mInitialContentHeaderPos
                            + contentHeader.getHeight());
                    fabPlayButton.setY(mInitialContentHeaderPos
                            + contentHeader.getHeight() - fabPlayButton.getHeight() / 2);
                }
                if (mIsHeaderSticky) {
                    contentHeader.setY(scrollY);
                    contentHeaderShadow.setY(scrollY + contentHeader.getHeight());
                    fabPlayButton.setY(scrollY + contentHeader.getHeight()
                            - fabPlayButton.getHeight() / 2);
                }

                // relocate the position of contentImage
                final float currPosY = contentImage.getY();
                final float futurePosY = currPosY + (deltaY / 2.f);

                if (futurePosY >= mMaxContentImagePos) {
                    contentImage.setY(mMaxContentImagePos);
                } else if (futurePosY <= 0) {
                    contentImage.setY(0.f);
                } else {
                    contentImage.setY(futurePosY);
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if (mSelectedVideo != null) {
            outState.putParcelable(KEY_BUNDLE_VIDEO, mSelectedVideo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, VideoBrowseActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
