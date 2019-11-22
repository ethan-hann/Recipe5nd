package com.uhcl.recipe5nd.helperClasses;

import java.util.Comparator;

public class SortBasedOnRecipe implements Comparator<Recipe>
{
    @Override
    public int compare(Recipe o1, Recipe o2)
    {
        return o1.getStrMeal().compareTo(o2.getStrMeal());
    }
}
