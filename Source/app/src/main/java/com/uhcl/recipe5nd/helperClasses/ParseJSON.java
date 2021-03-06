/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Ethan D. Hann
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uhcl.recipe5nd.helperClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ParseJSON
{
    private static final String TAG = "ParseJSON";

    /**
     * Parses recipe ids from API based on a supplied HTTP response string
     * @param idJSON : the HTTP response
     * @return ArrayList of ID strings
     * @throws JSONException : if the JSON is invalid or null and cannot be parsed successfully
     */
    public static ArrayList<String> parseIDS(String idJSON) throws JSONException
    {
        ArrayList<String> ids = new ArrayList<>();
        JSONObject obj = new JSONObject(idJSON);
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
     * Parses multiple recipes from a local saved JSON file
     * @param recipeJSON : the string to parse from
     * @return ArrayList of Recipes representing the parsed data
     * @throws JSONException : if the JSON is invalid or null and cannot be parsed successfully
     */
    public static ArrayList<Recipe> parseLocalRecipes(String recipeJSON) throws JSONException
    {
        ArrayList<Recipe> recipes = new ArrayList<>();
        JSONArray objArray = new JSONArray(recipeJSON);

        if (objArray.isNull(0)) {
            return null;
        }

        for (int i = 0; i < objArray.length(); i++)
        {
            Recipe r = new Recipe();
            JSONObject object = objArray.getJSONObject(i);
            r.setId(object.get("id").toString());
            r.setStrMeal(object.get("name").toString());
            r.setStrMealThumb(object.get("strThumbnail").toString());
            r.setStrYoutube(object.get("strYoutube").toString());

            JSONArray ingredients = object.getJSONArray("ingredients");
            for (int j = 0; j <= 20; j++)
            {
                try {
                    JSONObject ingObject = ingredients.getJSONObject(j);
                    if (ingObject.get("name").toString().equals("null") || ingObject.get("name").equals("")) {
                        break;
                    } else {
                        r.addIngredient(ingObject.get("name").toString(), ingObject.get("measure").toString());

                    }
                } catch (JSONException e) {
                    Log.e(TAG, "parseLocalRecipes: ", e);
                    break;
                }
            }

            r.setStrInstructions(object.get("instructions").toString());
            recipes.add(r);
        }

        return recipes;
    }


    /**
     * Parses recipe details from API based on a supplied HTTP response string
     * @param recipeJSON : the HTTP response
     * @return Recipe : a single recipe
     * @throws JSONException : if the JSON is invalid or null and cannot be parsed successfully
     */
    public static Recipe parseRecipe(String recipeJSON) throws JSONException
    {
        Recipe r = new Recipe();
        JSONObject obj = new JSONObject(recipeJSON);
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

            if (objArray.getJSONObject(0).get(getIngParam).toString().equals("null")) {
                break;
            }
            else
            {
                String name = objArray.getJSONObject(0).get(getIngParam).toString();
                String measurement = objArray.getJSONObject(0).get(getMeaParam).toString();
                r.addIngredient(name, measurement);
            }
        }

        return r;
    }

    /**
     * Parses ingredients from a local saved JSON file
     * @param ingredientsJSON : the string to parse from
     * @return ArrayList of Ingredients representing the parsed data
     * @throws JSONException : if the JSON is invalid or null and cannot be parsed successfully
     */
    public static ArrayList<Ingredient> parseIngredients(String ingredientsJSON) throws JSONException
    {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        JSONArray objArray = new JSONArray(ingredientsJSON);

        if (objArray.isNull(0)) {
            return null;
        }

        for (int i = 0; i < objArray.length(); i++)
        {
            Ingredient ing = new Ingredient();
            JSONObject object = objArray.getJSONObject(i);
            ing.setName(object.get("name").toString());

            ing.setPrimaryTag(PrimaryTag.valueOf(object.get("primaryTag").toString()));

            //Checking if the ingredient has an optional tag
            if (!object.isNull("optionalTag"))
            {
                ing.setOptionalTag(object.get("optionalTag").toString());
            }

            ingredients.add(ing);
        }

        return ingredients;
    }

    /**
     * Parses shopping lists from a local saved JSON file
     * @param shoppingJSON : the string to parse from
     * @return ArrayList of ShoppingList representing the parsed data
     * @throws JSONException : if the JSON is invalid or null and cannot be parsed successfully
     */
    public static ArrayList<ShoppingList> parseShoppingLists(String shoppingJSON) throws JSONException
    {
        SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
        JSONArray objArray = new JSONArray(shoppingJSON);

        if (objArray.isNull(0)) {
            return null;
        }

        for (int i = 0; i < objArray.length(); i++)
        {
            ShoppingList s = new ShoppingList();
            JSONObject object = objArray.getJSONObject(i);
            s.setTitle(object.get("title").toString());
            try {
                s.setDate(sdf.parse(object.get("date").toString()));
            } catch (ParseException e)
            {
                Log.e(TAG, "parseShoppingLists: ", e);
                s.setDate(new Date());
            }

            JSONArray itemArray = object.getJSONArray("items");
            JSONObject itemObject = itemArray.getJSONObject(0);

            String itemString = itemObject.get("names").toString();
            String itemStatesString = itemObject.get("isChecked").toString();
            String[] items = itemString.split(",");
            String[] itemStates = itemStatesString.split(",");

            for (String str : items)
            {
                s.addItem(str);
            }

            for (int j = 0; j < itemStates.length; j++) {
                s.setChecked(j, Boolean.parseBoolean(itemStates[j]));
            }

            shoppingLists.add(s);
        }

        return shoppingLists;
    }
}
