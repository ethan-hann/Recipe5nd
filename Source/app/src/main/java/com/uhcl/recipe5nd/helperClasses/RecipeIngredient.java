package com.uhcl.recipe5nd.helperClasses;

import androidx.annotation.NonNull;

public class RecipeIngredient
{
    private String name;
    private String measurement;

    public RecipeIngredient(String name, String measurement)
    {
        this.name = name;
        this.measurement = measurement;
    }

    public RecipeIngredient()
    {

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
