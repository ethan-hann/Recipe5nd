package com.uhcl.recipe5nd.helperClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

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

    private ArrayList<String> ingredients;
    private ArrayList<String> measurements;
    private ArrayList<String> instructions; //tokenized instructions from 'strInstructions'

    public Recipe() {
        ingredients = new ArrayList<>();
        measurements = new ArrayList<>();
        instructions = new ArrayList<>();
    }

    public ArrayList<String> tokenizeInstructions()
    {
        String[] tokens = strInstructions.split("\\r?\\n|\\r");
        instructions.addAll(Arrays.asList(tokens));

        return instructions;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public void addMeasurement(String measurement) {
        measurements.add(measurement);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public void setStrDrinkAlternative(String strDrinkAlternative) {
        this.strDrinkAlternative = strDrinkAlternative;
    }

    public void setStrCategory(String strCategory) {
        this.strCategory = strCategory;
    }

    public void setStrArea(String strArea) {
        this.strArea = strArea;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public void setStrTags(String strTags) {
        this.strTags = strTags;
    }

    public void setStrYoutube(String strYoutube) {
        this.strYoutube = strYoutube;
    }

    public void setStrSource(String strSource) {
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

    public String getStrInstructions() {
        return strInstructions;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public String getStrTags() {
        return strTags;
    }

    public String getStrYoutube() {
        return strYoutube;
    }

    public String getStrSource() {
        return strSource;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getMeasurements() {
        return measurements;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public String getRecipeInformation() {
        return String.format(Locale.US, "Meal ID: %s\nMeal Name: %s\nMeal Category: %s\n" +
                "Meal Locale: %s\nMeal Thumbnail: %s\nYouTube Video: %s", id, strMeal,
                strCategory, strArea, strMealThumb, strYoutube);
    }
}
