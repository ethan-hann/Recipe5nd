package com.uhcl.recipe5nd.helperClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateJSON
{
    private static final String TAG = "CreateJSON";

    public static String createRecipeJSON(ArrayList<Recipe> savedRecipes)
    {
        try {
            JSONArray recipes = new JSONArray();
            for (Recipe r : savedRecipes) {
                JSONObject recipeObject = new JSONObject();
                recipeObject.put("id", r.getId());
                recipeObject.put("name", r.getStrMeal());
                recipeObject.put("strThumbnail", r.getStrMealThumb());
                recipeObject.put("strYoutube", r.getStrYoutube());

                JSONArray ingredients = new JSONArray();
                for (int i = 0; i < r.getIngredients().size(); i++) {
                    JSONObject ingredientObject = new JSONObject();
                    ingredientObject.put("name", r.getIngredients().get(i));
                    ingredientObject.put("measure", r.getMeasurements().get(i));
                    ingredients.put(ingredientObject);
                }
                recipeObject.put("ingredients", ingredients);

                recipeObject.put("instructions", r.getStrInstructions());
                recipes.put(recipeObject);
            }

            return recipes.toString();
        } catch (JSONException e) {
            Log.e(TAG, "createRecipeJSON: ", e);
        }
        return "";
    }

    public static String createIngredientsJSON(ArrayList<Ingredient> savedIngredients)
    {
        //TODO: implement creation of Ingredients JSON
        try
        {
            JSONArray ingredients = new JSONArray();
            for (Ingredient i : savedIngredients)
            {
                JSONObject ingredientObject = new JSONObject();
                ingredientObject.put("name", i.getName());
                ingredientObject.put("primaryTag", i.getPrimaryTag().toString());
                ingredientObject.put("optionalTag", i.getOptionalTag());
                ingredients.put(ingredientObject);
            }

            return ingredients.toString();
        } catch (JSONException e)
        {
            Log.e(TAG, "createIngredientsJSON: ", e);
        }
        return "";
        
    }

    public static String createShoppingListsJSON()
    {
        //TODO: implement creation of Shopping Lists JSON
        return "";
    }
}
