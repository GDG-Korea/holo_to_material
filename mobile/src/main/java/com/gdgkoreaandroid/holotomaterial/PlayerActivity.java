package com.gdgkoreaandroid.holotomaterial;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.gdgkoreaandroid.holotomaterial.data.Video;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends ActionBarActivity {

    public static final String BUNDLE_KEY_VIDEO = "bundle_key_video";

    private static final String TAG = "PlayerActivity";

    private static final int HIDE_CONTROLLER_TIME = 5000;
    private static final int SEEKBAR_DELAY_TIME = 100;
    private static final int SEEKBAR_INTERVAL_TIME = 1000;
    private static final double MEDIA_BAR_TOP_MARGIN = 0.8;
    private static final double MEDIA_BAR_RIGHT_MARGIN = 0.2;
    private static final double MEDIA_BAR_BOTTOM_MARGIN = 0.0;
    private static final double MEDIA_BAR_LEFT_MARGIN = 0.2;
    private static final double MEDIA_BAR_HEIGHT = 0.1;
    private static final double MEDIA_BAR_WIDTH = 0.9;
    private final Handler mHandler = new Handler();
    private VideoView mVideoView;
    private TextView mStartText;
    private TextView mEndText;
    private SeekBar mSeekbar;
    private ImageView mPlayPause;
    private ProgressBar mLoading;
    private View mControllers;
    private Timer mSeekbarTimer;
    private Timer mControllersTimer;
    private PlaybackState mPlaybackState;
    private boolean mControlersVisible;
    private final View.OnClickListener mPlayPauseHandler = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mControlersVisible) {
                updateControllersVisibility(true);
            }

            if (mPlaybackState == PlaybackState.PAUSED) {
                mPlaybackState = PlaybackState.PLAYING;
                updatePlayButton(mPlaybackState);
                mVideoView.start();
                startControllersTimer();
            } else {
                mVideoView.pause();

                mPlaybackState = PlaybackState.PAUSED;
                updatePlayButton(PlaybackState.PAUSED);
                stopControllersTimer();
            }

        }
    };
    private int mDuration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        loadViews();
        setupController();
        setupControlsCallbacks();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopControllersTimer();
        stopSeekBarTimer();
    }

    private void loadViews() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mStartText = (TextView) findViewById(R.id.startText);
        mEndText = (TextView) findViewById(R.id.endText);
        mSeekbar = (SeekBar) findViewById(R.id.seekBar);
        mPlayPause = (ImageView) findViewById(R.id.playpause);
        mLoading = (ProgressBar) findViewById(R.id.progressBar);
        mControllers = findViewById(R.id.controllers);
        View container = findViewById(R.id.container);

        container.setOnClickListener(mPlayPauseHandler);
    }

    private void setupController() {

        final DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int w = (int) (metrics.widthPixels * MEDIA_BAR_WIDTH);
        int h = (int) (metrics.heightPixels * MEDIA_BAR_HEIGHT);
        int marginLeft = (int) (metrics.widthPixels * MEDIA_BAR_LEFT_MARGIN);
        int marginTop = (int) (metrics.heightPixels * MEDIA_BAR_TOP_MARGIN);
        int marginRight = (int) (metrics.widthPixels * MEDIA_BAR_RIGHT_MARGIN);
        int marginBottom = (int) (metrics.heightPixels * MEDIA_BAR_BOTTOM_MARGIN);
        LayoutParams lp = new LayoutParams(w, h);
        lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
        mControllers.setLayoutParams(lp);
        mStartText.setText(getResources().getString(R.string.init_text));
        mEndText.setText(getResources().getString(R.string.init_text));
    }

    private void setupControlsCallbacks() {

        mVideoView.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                String msg = "";
                if (extra == MediaPlayer.MEDIA_ERROR_TIMED_OUT) {
                    msg = getString(R.string.video_error_media_load_timeout);
                } else if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
                    msg = getString(R.string.video_error_server_unaccessible);
                } else {
                    msg = getString(R.string.video_error_unknown_error);
                }
                mVideoView.stopPlayback();
                mPlaybackState = PlaybackState.IDLE;

                if (!msg.isEmpty()) {
                    Log.w(TAG, "Fail to load the video: " + msg);
                }
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.d(TAG, "onPrepared is reached");
                mDuration = mp.getDuration();
                mEndText.setText(formatMillis(mDuration));
                mSeekbar.setMax(mDuration);
                restartSeekBarTimer();
            }
        });

        mVideoView.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSeekBarTimer();
                mPlaybackState = PlaybackState.IDLE;
                updatePlayButton(PlaybackState.IDLE);
            }
        });
    }

    /**
     * Formats time in milliseconds to hh:mm:ss string format.
     *
     * @param millis
     * @return
     */
    private String formatMillis(int millis) {
        String result = "";
        int hr = millis / 3600000;
        millis %= 3600000;
        int min = millis / 60000;
        millis %= 60000;
        int sec = millis / 1000;
        if (hr > 0) {
            result += hr + ":";
        }
        if (min >= 0) {
            if (min > 9) {
                result += min + ":";
            } else {
                result += "0" + min + ":";
            }
        }
        if (sec > 9) {
            result += sec;
        } else {
            result += "0" + sec;
        }
        return result;
    }

    private void restartSeekBarTimer() {
        stopSeekBarTimer();
        mSeekbarTimer = new Timer();
        mSeekbarTimer.scheduleAtFixedRate(new UpdateSeekbarTask(), SEEKBAR_DELAY_TIME,
                SEEKBAR_INTERVAL_TIME);
    }

    private void stopSeekBarTimer() {
        Log.d(TAG, "Stopped TrickPlay Timer");
        if (null != mSeekbarTimer) {
            mSeekbarTimer.cancel();
        }
    }

    private void updatePlayButton(PlaybackState state) {
        switch (state) {
            case PLAYING:
                mLoading.setVisibility(View.INVISIBLE);
                mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(
                        getResources().getDrawable(R.drawable.ic_pause_playcontrol_normal));
                break;
            case PAUSED:
            case IDLE:
                mLoading.setVisibility(View.INVISIBLE);
                mPlayPause.setVisibility(View.VISIBLE);
                mPlayPause.setImageDrawable(
                        getResources().getDrawable(R.drawable.ic_play_playcontrol_normal));
                break;
            case BUFFERING:
                mPlayPause.setVisibility(View.INVISIBLE);
                mLoading.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (null != mSeekbarTimer) {
            mSeekbarTimer.cancel();
            mSeekbarTimer = null;
        }
        if (null != mControllersTimer) {
            mControllersTimer.cancel();
        }
        mVideoView.pause();
        mPlaybackState = PlaybackState.PAUSED;
        updatePlayButton(PlaybackState.PAUSED);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startVideoPlayer();
        updateMetadata(true);
    }

    private void startVideoPlayer() {
        Bundle b = getIntent().getExtras();

        Video video = getIntent().getParcelableExtra(BUNDLE_KEY_VIDEO);
        boolean play = getIntent().getBooleanExtra("play", false);
        boolean pause = getIntent().getBooleanExtra("pause", false);

        if (play) {
            mPlaybackState = PlaybackState.PLAYING;
            updatePlayButton(mPlaybackState);
            mVideoView.start();
            startControllersTimer();
        } else if (pause) {
            mVideoView.pause();
            mPlaybackState = PlaybackState.PAUSED;
            updatePlayButton(PlaybackState.PAUSED);
            stopControllersTimer();
        }

        if (video != null) {
            Video selectedVideo = video;

            if (selectedVideo != null) {
                setTitle(selectedVideo.title);
                boolean shouldStartPlayback = b.getBoolean(getResources().getString(R.string.should_start));
                int startPosition = b.getInt(getResources().getString(R.string.start_position), 0);
                Log.d(TAG, "Try to play: " + selectedVideo.getVideoUri().toString());
                mVideoView.setVideoPath(selectedVideo.getVideoUri().toString());
                if (shouldStartPlayback) {
                    mPlaybackState = PlaybackState.PLAYING;
                    updatePlayButton(mPlaybackState);
                    if (startPosition > 0) {
                        mVideoView.seekTo(startPosition);
                    }
                    mVideoView.start();
                    mPlayPause.requestFocus();
                    startControllersTimer();
                } else {
                    updatePlaybackLocation();
                    mPlaybackState = PlaybackState.PAUSED;
                    updatePlayButton(mPlaybackState);
                }
            }
        }
    }

    private void updateMetadata(boolean visible) {
        mVideoView.invalidate();
    }

    private void startControllersTimer() {
        if (null != mControllersTimer) {
            mControllersTimer.cancel();
        }
        mControllersTimer = new Timer();
        mControllersTimer.schedule(new HideControllersTask(), HIDE_CONTROLLER_TIME);
    }

    private void stopControllersTimer() {
        if (null != mControllersTimer) {
            mControllersTimer.cancel();
        }
    }

    private void updatePlaybackLocation() {
        if (mPlaybackState == PlaybackState.PLAYING ||
                mPlaybackState == PlaybackState.BUFFERING) {
            startControllersTimer();
        } else {
            stopControllersTimer();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void play(int position) {
        startControllersTimer();
        mVideoView.seekTo(position);
        mVideoView.start();
        restartSeekBarTimer();
    }

    private void updateControllersVisibility(boolean show) {
        if (show) {
            mControllers.setVisibility(View.VISIBLE);
        } else {
            mControllers.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSeekbar(int position, int duration) {
        mSeekbar.setProgress(position);
        mSeekbar.setMax(duration);
        mStartText.setText(formatMillis(position));
        mEndText.setText(formatMillis(duration));
    }

    /*
     * List of various states that we can be in
     */
    public static enum PlaybackState {
        PLAYING, PAUSED, BUFFERING, IDLE;
    }

    private class HideControllersTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateControllersVisibility(false);
                    mControlersVisible = false;
                }
            });

        }
    }

    private class UpdateSeekbarTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    final int currentPos;
                    currentPos = mVideoView.getCurrentPosition();
                    updateSeekbar(currentPos, mDuration);
                }
            });
        }
    }
}
