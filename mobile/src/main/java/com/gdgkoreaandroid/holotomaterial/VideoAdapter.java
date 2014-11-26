package com.gdgkoreaandroid.holotomaterial;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdgkoreaandroid.holotomaterial.data.Category;
import com.gdgkoreaandroid.holotomaterial.data.Video;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private static final String TAG = "VideoAdapter";
    private final Category mCategory;
    private final OnItemClickListener mOnItemClickListener;
    private final Context mAppContext;

    public VideoAdapter(Context context, Category category, OnItemClickListener listener) {
        mCategory= category;
        mOnItemClickListener = listener;
        mAppContext = context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int index) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.grid_movie_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(mCategory.videos[holder.getPosition()]);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int index) {

        ImageView videoThumb = holder.mMovieThumb;
        TextView movieTitle = holder.mMovieTitle;

        Video video = mCategory.videos[index];


        Glide.with(mAppContext).
                fromUri().
                asBitmap().
                centerCrop().
                load(video.getCardImageUri()).
                into(videoThumb);

        //downloader.downloadImage(video.getCardImageUri(mCategory.category).toString(), videoThumb);
        movieTitle.setText(video.title);
    }

    @Override
    public int getItemCount() {
        return mCategory.videos.length;
    }

    public static class ViewHolder  extends RecyclerView.ViewHolder {

        private final ImageView mMovieThumb;
        private final TextView mMovieTitle;

        public ViewHolder(View v) {
            super(v);
            mMovieThumb = (ImageView) v.findViewById(R.id.movie_thumbnail);
            mMovieTitle = (TextView) v.findViewById(R.id.movie_title);
        }
    }

    public static interface OnItemClickListener {

        public void onItemClick(Video moive);
    }
}
