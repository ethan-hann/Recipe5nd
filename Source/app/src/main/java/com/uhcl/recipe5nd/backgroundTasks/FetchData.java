package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;

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
import java.util.Locale;

//TODO: make 'catch' branches more specific
public class FetchData extends AsyncTask<URL, Void, ArrayList<Recipe>>
{
    @Override
    protected ArrayList<Recipe> doInBackground(URL... urls)
    {
        ArrayList<Recipe> recipes = new ArrayList<>(); //the recipes to return

        //Connecting and accessing the database API
        try {
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

            //Add new recipe object to ArrayList based on parsed JSON
            for (int i = 0; i < objArray.length(); i++) {
                Recipe r = new Recipe();
                r.setId(objArray.getJSONObject(i).get("idMeal").toString());
                r.setStrMeal(objArray.getJSONObject(i).get("strMeal").toString());
                r.setStrDrinkAlternative(objArray.getJSONObject(i).get("strDrinkAlternate").toString());
                r.setStrCategory(objArray.getJSONObject(i).get("strCategory").toString());
                r.setStrArea(objArray.getJSONObject(i).get("strArea").toString());
                r.setStrInstructions(objArray.getJSONObject(i).get("strInstructions").toString());
                r.setStrMealThumb(objArray.getJSONObject(i).get("strMealThumb").toString());
                r.setStrTags(objArray.getJSONObject(i).get("strTags").toString());
                r.setStrYoutube(objArray.getJSONObject(i).get("strYoutube").toString());
                r.setStrSource(objArray.getJSONObject(i).get("strSource").toString());
                for (int j = 1; j <= 20; j++) {
                    String jString = String.format(Locale.US, "%d", j);
                    String getIngParam = "strIngredient" + jString;
                    String getMeaParam = "strIngredient" + jString;
                    r.addIngredient(objArray.getJSONObject(i).get(getIngParam).toString());
                    r.addMeasurement(objArray.getJSONObject(i).get(getMeaParam).toString());
                }
                recipes.add(r);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }
}
