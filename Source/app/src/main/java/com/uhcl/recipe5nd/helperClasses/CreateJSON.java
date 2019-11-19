package com.uhcl.recipe5nd.helperClasses;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class CreateJSON
{
    private static final String TAG = "CreateJSON";

    /**
     * Creates a JSON string representing a list of Recipe objects
     * This method will get the existing JSON string from the file if it exists and use that
     * as a basis for the new JSON string
     * @param context : the application's context
     * @param savedRecipes : an ArrayList containing Recipe objects
     * @return
     */
    public static String createRecipeJSON(Context context, ArrayList<Recipe> savedRecipes)
    {
        try {
            FileHelper fileHelper = new FileHelper();
            if (Constants.doesFavoritesExist)
            {
                String fileContents = fileHelper.readFile(context, Constants.FAVORITES_FILE_NAME);
                JSONArray recipes = new JSONArray(fileContents);
                for (Recipe r : savedRecipes)
                {
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
            }
            else
            {
                Log.i(TAG, "createRecipeJSON: " + Constants.FAVORITES_FILE_NAME + " does not exist. Creating new JSON string.");
                JSONArray recipes = new JSONArray();
                for (Recipe r : savedRecipes)
                {
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
            }
        } catch (JSONException e) {
            Log.e(TAG, "createRecipeJSON: ", e);
        }
        return "";
    }

    /**
     * Creates a JSON string representing a list of Ingredient objects
     * This method will get the existing JSON string from the file if it exists and use that
     * as a basis for the new JSON string
     * @param context : the application's context
     * @param savedIngredients : an ArrayList containing Ingredient objects
     * @param overwrite : if this method should take into account existing ingredients or create a
     *                  whole new string
     * @return string : a string representation of the formatted JSON
     */
    public static String createIngredientsJSON(Context context, ArrayList<Ingredient> savedIngredients, boolean overwrite)
    {
        try
        {
            FileHelper fileHelper = new FileHelper();
            if (!overwrite)
            {
                String fileContents = fileHelper.readFile(context, Constants.INGREDIENTS_FILE_NAME);
                JSONArray ingredients = new JSONArray(fileContents);
                for (Ingredient i : savedIngredients)
                {
                    JSONObject ingredientObject = new JSONObject();
                    ingredientObject.put("name", i.getName());
                    ingredientObject.put("primaryTag", i.getPrimaryTag().toString());
                    ingredientObject.put("optionalTag", i.getOptionalTag());
                    ingredients.put(ingredientObject);
                }
                return ingredients.toString();
            }
            else
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
            }
        } catch (JSONException e)
        {
            Log.e(TAG, "createIngredientsJSON: ", e);
        }
        return "";
    }

    public static String createShoppingListsJSON(ArrayList<ShoppingData> shoppingData)
    {
        //TODO: implement creation of Shopping Lists JSON
        try
        {
            JSONArray shoppingLists = new JSONArray();
            for (ShoppingData s : shoppingData)
            {
                JSONObject shoppingObject = new JSONObject();
                shoppingObject.put("title", s.getTitle());
                shoppingObject.put("date", s.getDate().toString());

                JSONArray shoppingItems = new JSONArray();
                JSONObject itemObject = new JSONObject();
                itemObject.put("names", s.getItems());
                itemObject.put("isChecked", s.isChecked());
                shoppingItems.put(itemObject);

                shoppingObject.put("items", shoppingItems);
            }

            return shoppingLists.toString();
        } catch (JSONException e)
        {
            Log.e(TAG, "createShoppingListsJSON: ", e);
        }
        return "";
    }
}
