package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;

import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

/**
 * Fetches recipe IDs from the API on a separate thread other than the UI thread.
 */
public class FetchIds extends AsyncTask<URL, Void, ArrayList<String>>
{
    @Override
    protected ArrayList<String> doInBackground(URL... urls)
    {
        ArrayList<String> ids = new ArrayList<>();

        try {
            boolean canConnect = APIConnector.executeQuery(urls[0]);
            if (canConnect) {
                ids = ParseJSON.parseIDS(APIConnector.apiResponse);
                return ids;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return ids;
    }
}