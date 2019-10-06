package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;

import com.uhcl.recipe5nd.helperClasses.FilterResult;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchIds extends AsyncTask<URL, Void, ArrayList<FilterResult>>
{
    @Override
    protected ArrayList<FilterResult> doInBackground(URL... urls)
    {
        ArrayList<FilterResult> results = new ArrayList<>(); //the recipes to return

        //Connecting and accessing the database API
        try {

            //GETTING FILTERED RESULTS FROM INGREDIENT SEARCH: RETURNS ONLY 3 JSON OBJECTS
            //strMeal, strMealThumb, and idMeal
            HttpURLConnection httpURLConnection = (HttpURLConnection) urls[0].openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            //Parse JSON from HTTP response
            JSONObject obj = new JSONObject(response.toString());
            JSONArray objArray = obj.getJSONArray("meals");
            if (objArray.isNull(0)) {
                return null;
            }

            for (int i = 0; i < objArray.length(); i++) {
                FilterResult f = new FilterResult();
                f.setId(objArray.getJSONObject(i).get("idMeal").toString());
                f.setStrMeal(objArray.getJSONObject(i).get("strMeal").toString());
                f.setStrMealThumb(objArray.getJSONObject(i).get("strMealThumb").toString());
                results.add(f);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }
}