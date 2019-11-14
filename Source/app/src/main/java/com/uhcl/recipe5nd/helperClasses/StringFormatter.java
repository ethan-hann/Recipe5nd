package com.uhcl.recipe5nd.helperClasses;

import java.util.ArrayList;
import java.util.Locale;

public class StringFormatter
{
    private static int getLongestMeasurementString(Recipe r)
    {
        int maxLength = 0;
        for (String s : r.getMeasurements())
        {
            if (s.length() > maxLength)
            {
                maxLength = s.length();
            }
        }
        return maxLength;
    }

    public static String formatRecipeIngredientsAndMeasures(Recipe r)
    {
        StringBuilder builder = new StringBuilder();
        int maxLength = getLongestMeasurementString(r);

        //Just a utility check, these should always be the same size
        if (r.getIngredients().size() == r.getMeasurements().size())
        {
            //builder.append("\n");
            for (int i = 0; i < r.getIngredients().size(); i++)
            {
                if (!r.getIngredients().get(i).equals(""))
                {
                    builder.append(r.getMeasurements().get(i));

                    //Seperating the measurements and ingredients based on longest measurement string
                    for (int j = 0; j < maxLength; j++)
                    {
                        builder.append(" ");
                    }

                    builder.append(r.getIngredients().get(i));
                    builder.append("\n");
                }
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
            System.out.println(instructions.get(i));
            builder.append(instructions.get(i));
            builder.append("\n");
        }

        return builder.toString();
    }
}
