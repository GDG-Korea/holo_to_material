package com.gdgkoreaandroid.holotomaterial;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgkoreaandroid.holotomaterial.data.Video;

/**
 * A fragment representing a single Video detail screen.
 * This fragment is either contained in a {@link VideoListActivity}
 * in two-pane mode (on tablets) or a {@link VideoDetailActivity}
 * on handsets.
 */
public class VideoDetailFragment extends Fragment {


    private static final String KEY_ARG_VIDEO = "key_arg_video";
    private Video mVideo;

    public VideoDetailFragment() {
    }

    public static VideoDetailFragment newInstance(Video video) {
        VideoDetailFragment fragment = new VideoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_ARG_VIDEO, video);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(KEY_ARG_VIDEO)) {
            mVideo = getArguments().getParcelable(KEY_ARG_VIDEO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_detail, container, false);

        if (mVideo != null) {
            TextView title = (TextView) rootView.findViewById(R.id.movie_detail_title);
            TextView meta = (TextView) rootView.findViewById(R.id.movie_detail_meta);
            TextView description = (TextView) rootView.findViewById(R.id.movie_detail_descritpion);
            ImageView playImageView = (ImageView) rootView.findViewById(R.id.movie_detail_play);
            View thumbContainer = rootView.findViewById(R.id.movie_detail_thumb_container);

            title.setText(mVideo.title);
            meta.setText(mVideo.studio);
            description.setText(mVideo.description);

            playImageView.setOnClickListener(mOnPlayVideoHandler);
            thumbContainer.setOnClickListener(mOnPlayVideoHandler);

        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mVideo != null) {
            final ImageView thumbnail = (ImageView) getView().findViewById(R.id.movie_detail_thumb);

            Glide.with(this).
                    fromUri().
                    asBitmap().
                    centerCrop().
                    load(mVideo.getBackgroundUri()).into(thumbnail);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private final View.OnClickListener mOnPlayVideoHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), PlayerActivity.class);
            intent.putExtra(PlayerActivity.BUNDLE_KEY_VIDEO, mVideo);
            intent.putExtra(v.getContext().getString(R.string.should_start), true);
            v.getContext().startActivity(intent);
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }
}
