package com.uhcl.recipe5nd.helperClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class ParseJSON
{
    private static FileHelper fileHelper = new FileHelper();

    /**
     * Parses recipe ids from API based on a supplied HTTP response string
     * @param response the HTTP response
     * @return ArrayList of ID strings
     * @throws JSONException if the JSON is invalid or null and cannot be parsed successfully
     */
    public static ArrayList<String> parseIDS(String response) throws JSONException
    {
        ArrayList<String> ids = new ArrayList<>();
        JSONObject obj = new JSONObject(response);
        JSONArray objArray = obj.getJSONArray("meals");

        if (objArray.isNull(0)) {
            return null;
        }

        for (int i = 0; i < objArray.length(); i++) {
            ids.add(objArray.getJSONObject(i).get("idMeal").toString());
        }

        return ids;
    }

    /**
     * Parses recipe details from API based on a supplied HTTP response string
     * @param response the HTTP response
     * @return Recipe a single recipe
     * @throws JSONException if the JSON is invalid or null and cannot be parsed successfully
     */
    public static Recipe parseRecipe(String response) throws JSONException
    {
        Recipe r = new Recipe();
        JSONObject obj = new JSONObject(response);
        JSONArray objArray = obj.getJSONArray("meals");

        if (objArray.isNull(0)) {
            return null;
        }

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

        return r;
    }

    /**
     * Parses ingredients from a local saved JSON file
     * @param ingredientFile the file to parse from
     * @return ArrayList of Ingredients representing the parsed data
     */
    public static ArrayList<Ingredient> parseIngredients(String ingredientFile) throws JSONException
    {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        JSONObject obj = new JSONObject(ingredientFile);
        JSONArray objArray = obj.getJSONArray("ingredients");

        if (objArray.isNull(0)) {
            return null;
        }

        Ingredient ing = new Ingredient();
        for (int i = 0; i < objArray.length(); i++) {
            ing.setName(objArray.getJSONObject(i).get("name").toString());
            ing.setPrimaryTag((PrimaryTag) objArray.getJSONObject(i).get("primaryTag"));
            ing.setOptionalTag(objArray.getJSONObject(i).get("optionalTag").toString());
            ingredients.add(ing);
        }

        return ingredients;
    }
}
