package com.uhcl.recipe5nd.helperClasses;

/**
 * An ingredient object is defined by this class. It is needed for the recycler view in @SearchFragment.
 */
public class Ingredient
{
    private String name;

    public Ingredient(String name) {
        this.name = name;
    }

    public Ingredient() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
