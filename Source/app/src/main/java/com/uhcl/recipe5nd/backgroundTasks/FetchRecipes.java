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
public class FetchRecipes extends AsyncTask<ArrayList<String>, Void, ArrayList<Recipe>>
{
    @Override
    protected ArrayList<Recipe> doInBackground(ArrayList<String>... ids)
    {
        ArrayList<Recipe> returnedRecipes = new ArrayList<>();
        ArrayList<String> responses = new ArrayList<>();

        //Connecting and accessing the database API
        try {
            for (int i = 0; i < ids[0].size(); i++) {

                //GETTING RECIPES BASED ON THEIR ID
                String url = Constants.BASE_URL
                        .concat(Constants.API_KEY)
                        .concat(Constants.LOOKUP_SUFFIX)
                        .concat(ids[0].get(i));
                URL query = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) query.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                responses.add(response.toString());
            }

            for (String s : responses) {
                //Parse JSON from HTTP responses
                JSONObject obj = new JSONObject(s);
                JSONArray objArray = obj.getJSONArray("meals");
                Recipe r = new Recipe();

                r.setId(objArray.getJSONObject(0).get("idMeal").toString());
                r.setStrMeal(objArray.getJSONObject(0).get("strMeal").toString());
                r.setStrDrinkAlternative(objArray.getJSONObject(0).get("strDrinkAlternate").toString());
                r.setStrCategory(objArray.getJSONObject(0).get("strCategory").toString());
                r.setStrArea(objArray.getJSONObject(0).get("strArea").toString());
                r.setStrInstructions(objArray.getJSONObject(0).get("strInstructions").toString());
                r.setStrMealThumb(objArray.getJSONObject(0).get("strMealThumb").toString());
                r.setStrTags(objArray.getJSONObject(0).get("strTags").toString());
                r.setStrYoutube(objArray.getJSONObject(0).get("strYoutube").toString());
                r.setStrSource(objArray.getJSONObject(0).get("strSource").toString());

                for (int j = 1; j <= 20; j++) {
                    String jString = String.format(Locale.US, "%d", j);
                    String getIngParam = "strIngredient" + jString;
                    String getMeaParam = "strMeasure" + jString;
                    r.addIngredient(objArray.getJSONObject(0).get(getIngParam).toString());
                    r.addMeasurement(objArray.getJSONObject(0).get(getMeaParam).toString());
                }
                returnedRecipes.add(r);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnedRecipes;
    }
}
