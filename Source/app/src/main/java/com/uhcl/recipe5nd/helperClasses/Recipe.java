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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import androidx.annotation.NonNull;

/**
 * Class to define a recipe as defined by the API. Used in the parsing of the returned JSON.
 */
public class Recipe
{
    private String id = "";
    private String strMeal = "";
    private String strDrinkAlternative = "";
    private String strCategory = "";
    private String strArea = "";
    private String strInstructions = "";
    private String strMealThumb = "";
    private String strTags = "";
    private String strYoutube = "";
    private String strSource = "";

    private ArrayList<RecipeIngredient> ingredientsAndMeasurements;
    private ArrayList<String> instructions; //tokenized instructions from 'strInstructions'

    public Recipe() {
        ingredientsAndMeasurements = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    ArrayList<String> tokenizeInstructions()
    {
        String[] tokens = strInstructions.split("\\r?\\n|\\r");
        instructions.addAll(Arrays.asList(tokens));

        return instructions;
    }

    public void addIngredient(RecipeIngredient i) {
        ingredientsAndMeasurements.add(i);
    }

    void addIngredient(String name, String measurement) {
        ingredientsAndMeasurements.add(new RecipeIngredient(name, measurement));
    }

    public void setId(String id) {
        this.id = id;
    }

    void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    void setStrDrinkAlternative(String strDrinkAlternative) {
        this.strDrinkAlternative = strDrinkAlternative;
    }

    void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    void setStrSource(String strSource) {
        this.strSource = strSource;
    }

    public String getId() {
        return id;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public String getStrDrinkAlternative() {
        return strDrinkAlternative;
    }

    public String getStrCategory() {
        return strCategory;
    }

    public String getStrArea() {
        return strArea;
    }

    String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    String getStrYoutube() {
        return strYoutube;
    }

    public String getStrSource() {
        return strSource;
    }

    public ArrayList<RecipeIngredient> getIngredientsAndMeasurements() {
        return ingredientsAndMeasurements;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.US, "Meal ID: %s\nMeal Name: %s\nMeal Category: %s\n" +
                        "Meal Locale: %s\nMeal Thumbnail: %s\nYouTube Video: %s", id, strMeal,
                strCategory, strArea, strMealThumb, strYoutube);
    }
}
