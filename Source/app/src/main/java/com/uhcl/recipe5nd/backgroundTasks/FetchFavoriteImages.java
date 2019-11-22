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

public class FetchFavoriteImages extends AsyncTask<String, Integer, ArrayList<Drawable>> {

    private static final String TAG = "DownloadFavoriteImage";

    @Override
    protected ArrayList<Drawable> doInBackground(String... imgURLS) {
        return downloadFavoriteImages(imgURLS);
    }

    private ArrayList<Drawable> downloadFavoriteImages(String[] urls) {

        if (Constants.favoriteRecipeImages == null) {
            Constants.favoriteRecipeImages = new ArrayList<>();
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

                Constants.favoriteRecipeImages.add(new BitmapDrawable(bMap));
                //return new BitmapDrawable(bMap);
            } catch (IOException e) {
                Log.e(TAG, "Error downloading image file:", e);
            }
        }
        return Constants.favoriteRecipeImages;
    }
}
