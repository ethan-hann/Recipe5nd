package com.uhcl.recipe5nd.helperClasses;

import androidx.annotation.NonNull;

/**
 * A local recipe ingredient is defined by this class. This is used in the searching and the
 * saving of recipes
 */
public class RecipeIngredient
{
    private String name;
    private String measurement;

    RecipeIngredient(String name, String measurement)
    {
        this.name = name;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s\t\t%s", measurement, name);
    }
}
