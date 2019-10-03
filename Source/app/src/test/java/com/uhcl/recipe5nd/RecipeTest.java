package com.uhcl.recipe5nd;

import com.uhcl.recipe5nd.helperClasses.Recipe;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeTest {
    private Recipe r = new Recipe();

    @Test
    public void createNewRecipe() {

        assertNotNull(r);
        assertEquals(Recipe.class, r.getClass());
    }

    @Test
    public void checkNullOnIngredients() {
        assertNotNull(r.getIngredients());
    }

    @Test
    public void checkNullOnMeasurements() {
        assertNotNull(r.getMeasurements());
    }

    @Test
    public void checkNullOnInstructions() {
        assertNotNull(r.getInstructions());
    }
}