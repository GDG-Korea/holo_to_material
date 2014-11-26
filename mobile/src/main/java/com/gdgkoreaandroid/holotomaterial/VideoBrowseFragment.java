package com.gdgkoreaandroid.holotomaterial;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gdgkoreaandroid.holotomaterial.data.Category;
import com.gdgkoreaandroid.holotomaterial.data.Video;

import static android.support.v7.widget.RecyclerView.OnScrollListener;

/**
 * A VideoList fragment representing a MOVIE_LIST of Movies. This fragment
 * also supports tablet devices by allowing MOVIE_LIST items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link VideoDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class VideoBrowseFragment extends Fragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    private static final String BUNDLE_KEY_CATEGORY = "key_category";

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(View view, Video id) {
        }

        @Override
        public OnScrollListener getScrollListener() {
            return null;
        }
    };
    /**
     * The fragment's current callback object, which is notified of MOVIE_LIST item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;
    private OnScrollListener mScrollListener = null;
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private Category mCategory;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public VideoBrowseFragment() {
    }

    public static VideoBrowseFragment newInstance(
            Category category) {
        VideoBrowseFragment fragment = new VideoBrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY_CATEGORY, category);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
        setOnScrollListener(mCallbacks.getScrollListener());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCategory = bundle.getParcelable(BUNDLE_KEY_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_video_browse, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        VideoAdapter adapter = new VideoAdapter(getActivity(), mCategory,
                new VideoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, Video video) {
                        mCallbacks.onItemSelected(v, video);
                    }
                });

        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setOnScrollListener(mScrollListener);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    private void setActivatedPosition(int position) {
        mActivatedPosition = position;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mScrollListener = listener;

        if (mRecyclerView != null) {
            mRecyclerView.setOnScrollListener(listener);
        }
    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(View view, Video id);

        public OnScrollListener getScrollListener();
    }

}
