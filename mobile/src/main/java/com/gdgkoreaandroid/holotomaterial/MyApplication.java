package com.gdgkoreaandroid.holotomaterial;

import android.app.Application;

import com.gdgkoreaandroid.holotomaterial.data.Videos;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Videos.initialize(getResources());
    }
}
