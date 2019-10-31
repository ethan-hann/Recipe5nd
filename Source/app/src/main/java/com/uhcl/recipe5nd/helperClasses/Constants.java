package com.uhcl.recipe5nd.helperClasses;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Constants
{
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v2/9973533";
    public static final String NAME_SUFFIX = "/search.php?s="; //can search by meal name or first letter
    public static final String ID_SUFFIX = "/lookup.php?i="; //can lookup meals by their id
    public static final String INGREDIENT_SEARCH_SUFFIX = "/filter.php?i="; //can search by ingredient(s)

    public static ArrayList<Drawable> returnedRecipeImages;
    public static ArrayList<Ingredient> selectedIngredients;
    public static ArrayList<Recipe> returnedRecipesFromSearch;
}
