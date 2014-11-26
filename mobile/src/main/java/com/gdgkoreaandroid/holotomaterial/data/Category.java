package com.gdgkoreaandroid.holotomaterial.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Category implements Parcelable {

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
    public String category;
    public Video[] videos;

    public Category(Parcel in) {

        category = in.readString();
        Object[] objArrays = in.readArray(Video.class.getClassLoader());
        videos = new Video[objArrays.length];
        videos = Arrays.copyOf(objArrays, objArrays.length, Video[].class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(category);
        dest.writeArray(videos);
    }

    public void initVideos() {
        for (Video video : videos) {
            video.setCateogry(category);

            // In order to make the description long enough to scroll up and down,
            // append pre-defined extra strings to the video description.

            video.description += "\n\n" + video.description.substring(30)
                    + video.description.substring(50);
        }
    }
}
