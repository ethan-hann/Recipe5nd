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
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Global file to help facilitate passing data between various fragments.
 */
public class Global
{
    static final String BASE_URL = "https://www.themealdb.com/api/json/v2/9973533";
    static final String NAME_SUFFIX = "/search.php?s="; //can search by meal name or first letter
    static final String ID_SUFFIX = "/lookup.php?i="; //can lookup meals by their id
    static final String INGREDIENT_SEARCH_SUFFIX = "/filter.php?i="; //can search by ingredient(s)

    static final Pattern validator = Pattern.compile("[^a-zA-Z ]");

    public static final String INGREDIENTS_FILE_NAME = "ingredients.json";
    public static final String FAVORITES_FILE_NAME = "favorites.json";
    public static final String SHOPPING_LIST_FILE_NAME = "shopping.json";

    public static boolean doesIngredientsFileExist;
    public static boolean doesFavoritesExist;
    public static boolean doesShoppingListExist;

    public static Recipe currentlyViewedRecipe;

    public static ArrayList<Recipe> favoriteRecipes = new ArrayList<>();

    public static ArrayList<Recipe> returnedRecipesFromSearch = new ArrayList<>();
    public static ArrayList<Drawable> returnedRecipeImages = new ArrayList<>();

    public static ArrayList<Ingredient> selectedIngredients = new ArrayList<>();
    public static ArrayList<Ingredient> usersIngredients = new ArrayList<>();

    public static ArrayList<ShoppingList> shoppingLists = new ArrayList<>();
    public static ShoppingList currentlyViewedShoppingList;


    public static void init(Context context) {
        FileHelper fileHelper = new FileHelper();
        fileHelper.createIfNotExists(context, Global.INGREDIENTS_FILE_NAME);
        fileHelper.createIfNotExists(context, Global.FAVORITES_FILE_NAME);
        fileHelper.createIfNotExists(context, Global.SHOPPING_LIST_FILE_NAME);
    }
}
