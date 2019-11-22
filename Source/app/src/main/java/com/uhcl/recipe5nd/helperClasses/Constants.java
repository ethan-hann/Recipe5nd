package com.uhcl.recipe5nd.helperClasses;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Constants
{
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v2/9973533";
    public static final String NAME_SUFFIX = "/search.php?s="; //can search by meal name or first letter
    public static final String ID_SUFFIX = "/lookup.php?i="; //can lookup meals by their id
    public static final String INGREDIENT_SEARCH_SUFFIX = "/filter.php?i="; //can search by ingredient(s)

    public static final Pattern validator = Pattern.compile("[^a-zA-Z ]");

    public static final String INGREDIENTS_FILE_NAME = "ingredients.json";
    public static final String FAVORITES_FILE_NAME = "favorites.json";
    public static final String SHOPPING_LIST_FILE_NAME = "shopping.json";

    public static boolean doesIngredientsFileExist;
    public static boolean doesFavoritesExist;
    public static boolean doesShoppingListExist;

    public static Recipe currentlyViewedRecipe;

    public static ArrayList<Recipe> favoriteRecipes;

    public static ArrayList<Recipe> returnedRecipesFromSearch;
    public static ArrayList<Drawable> returnedRecipeImages;

    public static ArrayList<Ingredient> selectedIngredients;
    public static ArrayList<Ingredient> usersIngredients;

    public static ArrayList<ShoppingList> shoppingLists;


    public static void init(Context context) {
        FileHelper fileHelper = new FileHelper();
        fileHelper.createIfNotExists(context, Constants.INGREDIENTS_FILE_NAME);
        fileHelper.createIfNotExists(context, Constants.FAVORITES_FILE_NAME);
        fileHelper.createIfNotExists(context, Constants.SHOPPING_LIST_FILE_NAME);

    }
}
