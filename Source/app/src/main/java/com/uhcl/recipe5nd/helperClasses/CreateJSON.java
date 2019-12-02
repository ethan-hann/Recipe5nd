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

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CreateJSON
{
    private static final String TAG = "CreateJSON";

    /**
     * Creates a JSON string representing a list of Recipe objects
     * This method can get the existing JSON string from the file if it exists and use that
     * as a basis for the new JSON string
     * @param context : the application's context
     * @param savedRecipes : an ArrayList containing Recipe objects
     * @param overwrite : if this method should take into account existing ingredients or create a
     *                    whole new string
     * @return string : a string representation of the formatted JSON
     */
    public static String createRecipeJSON(Context context, ArrayList<Recipe> savedRecipes, boolean overwrite)
    {
        try {
            FileHelper fileHelper = new FileHelper();
            if (!overwrite)
            {
                String fileContents = fileHelper.readFile(context, Global.FAVORITES_FILE_NAME);
                JSONArray recipes = new JSONArray(fileContents);
                for (Recipe r : savedRecipes)
                {
                    JSONObject recipeObject = new JSONObject();
                    recipeObject.put("id", r.getId());
                    recipeObject.put("name", r.getStrMeal());
                    recipeObject.put("strThumbnail", r.getStrMealThumb());
                    recipeObject.put("strYoutube", r.getStrYoutube());

                    JSONArray ingredients = new JSONArray();
                    for (int i = 0; i < r.getIngredientsAndMeasurements().size(); i++) {
                        JSONObject ingredientObject = new JSONObject();
                        ingredientObject.put("name",
                                r.getIngredientsAndMeasurements().get(i).getName());
                        ingredientObject.put("measure",
                                r.getIngredientsAndMeasurements().get(i).getMeasurement());
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
                JSONArray recipes = new JSONArray();
                for (Recipe r : savedRecipes)
                {
                    JSONObject recipeObject = new JSONObject();
                    recipeObject.put("id", r.getId());
                    recipeObject.put("name", r.getStrMeal());
                    recipeObject.put("strThumbnail", r.getStrMealThumb());
                    recipeObject.put("strYoutube", r.getStrYoutube());

                    JSONArray ingredients = new JSONArray();
                    for (int i = 0; i < r.getIngredientsAndMeasurements().size(); i++) {
                        JSONObject ingredientObject = new JSONObject();
                        ingredientObject.put("name",
                                r.getIngredientsAndMeasurements().get(i).getName());
                        ingredientObject.put("measure",
                                r.getIngredientsAndMeasurements().get(i).getMeasurement());
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
     * This method can get the existing JSON string from the file if it exists and use that
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
                String fileContents = fileHelper.readFile(context, Global.INGREDIENTS_FILE_NAME);
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

    /**
     * Creates a JSON string representing a list of Ingredient objects
     * This method can get the existing JSON string from the file if it exists and use that
     * as a basis for the new JSON string
     * @param context : the application's context
     * @param shoppingData : an ArrayList containing ShoppingList objects
     * @param overwrite : if this method should take into account existing ShoppingLists or create a
     *      *                  whole new string
     * @return string : a string representation of the formatted JSON
     */
    public static String createShoppingListsJSON(Context context, ArrayList<ShoppingList> shoppingData, boolean overwrite)
    {
        try
        {
            FileHelper fileHelper = new FileHelper();
            if (!overwrite)
            {
                String fileContents = fileHelper.readFile(context, Global.SHOPPING_LIST_FILE_NAME);
                JSONArray shoppingLists = new JSONArray(fileContents);

                for (ShoppingList s : shoppingData)
                {
                    JSONObject shoppingObject = new JSONObject();
                    shoppingObject.put("title", s.getTitle());
                    shoppingObject.put("date", s.getDate().toString());

                    JSONArray shoppingItems = new JSONArray();
                    JSONObject itemObject = new JSONObject();

                    //Getting items in the shopping list
                    StringBuilder builder = new StringBuilder();
                    if (s.getItems().size() != 0) {
                        for (int i = 0; i < s.getItems().size() - 1; i++) {
                            builder.append(s.getItems().get(i));
                            builder.append(",");
                        }
                        builder.append(s.getItems().get(s.getItems().size() - 1));
                        itemObject.put("names", builder.toString());
                    }

                    //Getting checked values in the shopping list
                    builder = new StringBuilder();
                    if (s.getIsCheckedArray().size() != 0) {
                        for (int i = 0; i < s.getIsCheckedArray().size() - 1; i++) {
                            builder.append(s.isChecked(i));
                            builder.append(",");
                        }
                        builder.append(s.getIsCheckedArray().get(s.getIsCheckedArray().size() - 1));
                        itemObject.put("isChecked", builder.toString());
                    }

                    shoppingItems.put(itemObject);

                    shoppingObject.put("items", shoppingItems);

                    shoppingLists.put(shoppingObject);
                }
                return shoppingLists.toString();
            }
            else
            {
                JSONArray shoppingLists = new JSONArray();
                for (ShoppingList s : shoppingData)
                {
                    JSONObject shoppingObject = new JSONObject();
                    shoppingObject.put("title", s.getTitle());
                    shoppingObject.put("date", s.getDate().toString());

                    JSONArray shoppingItems = new JSONArray();
                    JSONObject itemObject = new JSONObject();

                    //Getting items in the shopping list
                    StringBuilder builder = new StringBuilder();
                    if (s.getItems().size() != 0) {
                        for (int i = 0; i < s.getItems().size() - 1; i++) {
                            builder.append(s.getItems().get(i));
                            builder.append(",");
                        }
                        builder.append(s.getItems().get(s.getItems().size() - 1));
                        itemObject.put("names", builder.toString());
                    }

                    //Getting checked values in the shopping list
                    builder = new StringBuilder();
                    if (s.getIsCheckedArray().size() != 0) {
                        for (int i = 0; i < s.getIsCheckedArray().size() - 1; i++) {
                            builder.append(s.isChecked(i));
                            builder.append(",");
                        }
                        builder.append(s.getIsCheckedArray().get(s.getIsCheckedArray().size() - 1));
                        itemObject.put("isChecked", builder.toString());
                    }

                    shoppingItems.put(itemObject);

                    shoppingObject.put("items", shoppingItems);

                    shoppingLists.put(shoppingObject);
                }
                return shoppingLists.toString();
            }
        } catch (JSONException e)
        {
            Log.e(TAG, "createShoppingListsJSON: ", e);
        }
        return "";
    }
}
