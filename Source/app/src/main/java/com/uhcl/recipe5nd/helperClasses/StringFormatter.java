package com.uhcl.recipe5nd.helperClasses;

import java.util.ArrayList;

public class StringFormatter
{
    public static String formatRecipeIngredientsAndMeasures(Recipe r)
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < r.getIngredientsAndMeasurements().size(); i++) {
            builder.append(r.getIngredientsAndMeasurements().get(i).toString());
            builder.append("\n");
        }

        return builder.toString();
    }

    public static String formatRecipeSteps(Recipe r)
    {
        StringBuilder builder = new StringBuilder();

        ArrayList<String> instructions = r.tokenizeInstructions();
        for (int i = 0; i < instructions.size(); i++)
        {
            builder.append(instructions.get(i));
            builder.append("\n");
        }

        return builder.toString();
    }
}
