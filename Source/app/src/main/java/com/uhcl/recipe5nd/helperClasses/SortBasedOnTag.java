package com.uhcl.recipe5nd.helperClasses;

import java.util.Comparator;

public class SortBasedOnTag implements Comparator<Ingredient>
{
    @Override
    public int compare(Ingredient t1, Ingredient t2)
    {
        return t1.getPrimaryTag().compareTo(t2.getPrimaryTag());
    }
}
