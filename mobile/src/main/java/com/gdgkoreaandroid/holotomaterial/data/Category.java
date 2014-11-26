package com.gdgkoreaandroid.holotomaterial.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {

    public String category;
    public Video[] videos;

    public Category(Parcel in) {

        category = in.readString();
        videos = (Video[]) in.readParcelableArray(Video.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(category);
        dest.writeArray(videos);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void initVideos() {
        for (Video video : videos) {
            video.setCateogry(category);
        }
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };
}
