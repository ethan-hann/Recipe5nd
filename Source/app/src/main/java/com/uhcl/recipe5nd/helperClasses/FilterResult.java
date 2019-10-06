package com.uhcl.recipe5nd.helperClasses;

public class FilterResult
{
    private String id;
    private String strMealThumb;
    private String strMeal;

    public FilterResult(String id, String strMeal, String strMealThumb) {
        this.id = id;
        this.strMeal = strMeal;
        this.strMealThumb = strMealThumb;
    }

    public FilterResult() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }
}
