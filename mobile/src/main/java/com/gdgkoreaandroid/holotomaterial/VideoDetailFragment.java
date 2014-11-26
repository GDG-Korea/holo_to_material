package com.gdgkoreaandroid.holotomaterial;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdgkoreaandroid.holotomaterial.data.Video;

/**
 * A fragment representing a single Video detail screen.
 * This fragment is either contained in a {@link VideoBrowseActivity}
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_video_detail, container, false);

        if (mVideo != null) {
            TextView description = (TextView) rootView.findViewById(R.id.content_body);
            description.setText(mVideo.description);
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.my, menu);
    }
}
