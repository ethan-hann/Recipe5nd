package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

/**
 * Fetches recipes from the API on a separate thread other than the UI thread.
 */
public class FetchRecipe extends AsyncTask<URL, Void, ArrayList<Recipe>>
{
    private static final String TAG = "FetchRecipe";
    @Override
    protected ArrayList<Recipe> doInBackground(URL... urls)
    {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {

            for (int i = 0; i < urls.length; i++) {
                boolean canConnect = APIConnector.executeQuery(urls[i]);
                if (canConnect) {
                    recipes.add(ParseJSON.parseRecipe(APIConnector.apiResponse));
                }
            }
            return recipes;

        } catch (JSONException e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
    }
}
