package com.uhcl.recipe5nd.backgroundTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.uhcl.recipe5nd.helperClasses.Constants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import androidx.core.graphics.BitmapCompat;

public class FetchImages extends AsyncTask<String, Integer, ArrayList<Drawable>> {

    private static final String TAG = "DownloadImage";
    @Override
    protected ArrayList<Drawable> doInBackground(String... imgURLS) {
        return downloadImages(imgURLS);
    }

    private ArrayList<Drawable> downloadImages(String[] urls) {
        for (String s : urls) {
            System.out.println(s);
        }

        if (Constants.returnedRecipeImages == null) {
            Constants.returnedRecipeImages = new ArrayList<>();
        }

        URL imageURL;
        InputStream in;
        BufferedInputStream bufferedInputStream;

        for (String s : urls)
        {
            try {
                imageURL = new URL(s);
                in = imageURL.openStream();
                bufferedInputStream = new BufferedInputStream(in);

                Bitmap bMap = BitmapFactory.decodeStream(bufferedInputStream);
                in.close();
                bufferedInputStream.close();

                Constants.returnedRecipeImages.add(new BitmapDrawable(bMap));
                //return new BitmapDrawable(bMap);
            } catch (IOException e) {
                Log.e(TAG, "Error downloading image file:", e);
            }
        }
        return Constants.returnedRecipeImages;
    }
}
