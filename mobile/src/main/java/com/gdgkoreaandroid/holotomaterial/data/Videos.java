package com.gdgkoreaandroid.holotomaterial.data;

import android.content.res.Resources;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class Videos {

    private static ArrayList<Category> sCategories = new ArrayList<Category>();
    private static boolean sInitialized = false;

    public static Category[] getCategoryArray() {
        if (!sInitialized) {
            return new Category[]{};
        }

        return sCategories.toArray(new Category[]{});

    }

    public static Video[] getVideoArray() {
        if (!sInitialized) {
            return new Video[]{};
        }

        return sCategories.get(0).videos;
    }

    public static synchronized void initialize(Resources resources) {

        if (sInitialized) {
            return;
        }

        sInitialized = true;

        Gson gson = new Gson();
        InputStream is;
        try {
            is = resources.getAssets().open("videos.json");
            InputStreamReader isr = new InputStreamReader(is);
            Category[] categories = gson.fromJson(isr, Category[].class);

            for (Category category : categories) {
                category.initVideos();
                sCategories.add(category);
            }

            isr.close();
            is.close();
        } catch (IOException e) {
            //IGNORED
        }
    }
}