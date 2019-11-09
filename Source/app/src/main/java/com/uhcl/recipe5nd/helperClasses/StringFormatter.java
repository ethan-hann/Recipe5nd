package com.uhcl.recipe5nd.helperClasses;

import java.util.ArrayList;
import java.util.Locale;

public class StringFormatter
{
    public static String formatRecipeIngredientsAndMeasures(Recipe r)
    {
        StringBuilder builder = new StringBuilder();

        if (r.getIngredients().size() == r.getMeasurements().size())
        {
            builder.append("\n");
            for (int i = 0; i < r.getIngredients().size(); i++)
            {
                //TODO: Figure out why sometimes getting 'null' as a value
                //if (r.getIngredients().get(i).equals("null"))
                builder.append(r.getIngredients().get(i))
                        .append("\t\t\t\t\t\t\t\t\t\t\t\t")
                        .append(r.getMeasurements().get(i));
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public static String formatRecipeSteps(Recipe r)
    {
        StringBuilder builder = new StringBuilder();

        ArrayList<String> instructions = r.tokenizeInstructions();
        for (int i = 0; i < instructions.size(); i++)
        {
            builder.append(String.format(Locale.US, "%d. ", i + 1))
                    .append(instructions.get(i));
            builder.append("\n");
        }

        return builder.toString();
    }
}
