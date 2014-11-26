package com.gdgkoreaandroid.holotomaterial.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {

    public static final Parcelable.Creator<Video> CREATOR = new Parcelable.Creator<Video>() {
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    private final static Uri PREFIX_URI;
    static {
        PREFIX_URI = Uri.parse(
                "http://commondatastorage.googleapis.com/android-tv/Sample%20videos");
    }
    public String description;
    public String[] sources;
    public String card;
    public String background;
    public String title;
    public String studio;
    private String mCateogry;   //this field will be set by Category class.

    public Video(Parcel in) {
        mCateogry = in.readString();
        description = in.readString();
        sources = in.createStringArray();
        card = in.readString();
        background = in.readString();
        title = in.readString();
        studio = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCateogry);
        dest.writeString(description);
        dest.writeStringArray(sources);
        dest.writeString(card);
        dest.writeString(background);
        dest.writeString(title);
        dest.writeString(studio);
    }

    public void setCateogry(String cateogry) {
        mCateogry = cateogry;
    }

    public Uri getBackgroundUri() {
        Uri.Builder builder = PREFIX_URI.buildUpon();
        builder.appendPath(mCateogry);
        builder.appendPath(title);
        builder.appendPath(background);
        return builder.build();
    }

    public Uri getCardImageUri() {
        Uri.Builder builder = PREFIX_URI.buildUpon();
        builder.appendPath(mCateogry);
        builder.appendPath(title);
        builder.appendPath(card);
        return builder.build();
    }

    public Uri getVideoUri() {
        Uri.Builder builder = PREFIX_URI.buildUpon();
        builder.appendPath(mCateogry);
        builder.appendPath(sources[0]);
        return builder.build();
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", videoUrl='" + sources[0] + '\'' +
                ", backgroundImageUrl='" + background + '\'' +
                ", cardImageUrl='" + card + '\'' +
                '}';
    }
}
