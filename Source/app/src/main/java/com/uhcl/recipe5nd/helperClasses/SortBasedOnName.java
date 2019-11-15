package com.uhcl.recipe5nd.helperClasses;

import java.util.Comparator;

public class SortBasedOnName implements Comparator<Ingredient>
{
    @Override
    public int compare(Ingredient ingredient, Ingredient t1) {
        return ingredient.getName().compareToIgnoreCase(t1.getName());
    }
}
