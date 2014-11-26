package com.gdgkoreaandroid.holotomaterial;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.gdgkoreaandroid.holotomaterial.data.Category;
import com.google.gson.Gson;

import junit.framework.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private static final String TAG = "TEST";

    public ApplicationTest() {
        super(Application.class);
    }


    public void testMovieJsonAssetFile() {
        Gson gson = new Gson();
        InputStream is;
        try {
            is = getContext().getAssets().open("videos.json");
            InputStreamReader isr = new InputStreamReader(is);
            Category[] categories = gson.fromJson(isr, Category[].class);

            for (Category category : categories) {
                Log.d(TAG, "Category:" + category.category + ", " + category.videos.length);
            }

            Assert.assertEquals(4, categories.length);

            isr.close();
            is.close();
        } catch (IOException e) {
            //IGNORED
        }
    }
}