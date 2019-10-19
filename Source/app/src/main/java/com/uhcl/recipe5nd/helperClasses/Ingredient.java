package com.uhcl.recipe5nd.helperClasses;

/**
 * An ingredient object is defined by this class. It is needed for the recycler view in @SearchFragment.
 */
public class Ingredient
{
    private String name;
    private PrimaryTag primaryTag;
    private String optionalTag;

    public Ingredient(String name, PrimaryTag pTag, String oTag) {
        this.name = name;
        this.primaryTag = pTag;
        this.optionalTag = oTag;
    }

    public Ingredient(String name, PrimaryTag pTag) {
        this.name = name;
        this.primaryTag = pTag;
    }

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

    public PrimaryTag getPrimaryTag() {
        return primaryTag;
    }

    public void setPrimaryTag(PrimaryTag primaryTag) {
        this.primaryTag = primaryTag;
    }

    public String getOptionalTag() {
        return optionalTag;
    }

    public void setOptionalTag(String optionalTag) {
        this.optionalTag = optionalTag;
    }
}
