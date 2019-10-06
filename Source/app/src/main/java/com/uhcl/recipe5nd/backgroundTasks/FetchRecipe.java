package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;

import com.uhcl.recipe5nd.helperClasses.Constants;
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
import java.util.Locale;

//TODO: make 'catch' branches more specific
public class FetchRecipe extends AsyncTask<String, Void, Recipe>
{
    @Override
    protected Recipe doInBackground(String... ids)
    {
        Recipe returnedRecipe = new Recipe(); //the recipe to return

        //Connecting and accessing the database API
        try {
            //GETTING A RECIPE BASED ON ITS ID
            String url = Constants.BASE_URL
                    .concat(Constants.API_KEY)
                    .concat(Constants.LOOKUP_SUFFIX)
                    .concat(ids[0]);
            URL query = new URL(url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) query.openConnection();
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

            returnedRecipe.setId(objArray.getJSONObject(0).get("idMeal").toString());
            returnedRecipe.setStrMeal(objArray.getJSONObject(0).get("strMeal").toString());
            returnedRecipe.setStrDrinkAlternative(objArray.getJSONObject(0).get("strDrinkAlternate").toString());
            returnedRecipe.setStrCategory(objArray.getJSONObject(0).get("strCategory").toString());
            returnedRecipe.setStrArea(objArray.getJSONObject(0).get("strArea").toString());
            returnedRecipe.setStrInstructions(objArray.getJSONObject(0).get("strInstructions").toString());
            returnedRecipe.setStrMealThumb(objArray.getJSONObject(0).get("strMealThumb").toString());
            returnedRecipe.setStrTags(objArray.getJSONObject(0).get("strTags").toString());
            returnedRecipe.setStrYoutube(objArray.getJSONObject(0).get("strYoutube").toString());
            returnedRecipe.setStrSource(objArray.getJSONObject(0).get("strSource").toString());

            for (int i = 1; i <= 20; i++) {
                String jString = String.format(Locale.US, "%d", i);
                String getIngParam = "strIngredient" + jString;
                String getMeaParam = "strMeasure" + jString;
                returnedRecipe.addIngredient(objArray.getJSONObject(0).get(getIngParam).toString());
                returnedRecipe.addMeasurement(objArray.getJSONObject(0).get(getMeaParam).toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnedRecipe;
    }
}
