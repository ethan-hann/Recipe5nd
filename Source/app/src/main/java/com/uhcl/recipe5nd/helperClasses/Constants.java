package com.uhcl.recipe5nd.helperClasses;

import java.util.ArrayList;

public class Constants
{
    public static final String API_KEY = "9973533";
    public static final String BASE_URL = "https://www.themealdb.com/api/json/v2/";
    public static final String SEARCH_SUFFIX = "/search.php?s="; //can search by meal name or first letter
    public static final String LOOKUP_SUFFIX = "/lookup.php?i="; //can lookup meals by their id
    public static final String FILTER_SUFFIX = "/filter.php?i="; //filter search by ingredient(s)

    public static final int SEARCH_TIMEOUT = 5;

    public static ArrayList<Ingredient> selectedIngredients;
}
