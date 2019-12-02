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

/**
 * Class used to format recipe strings for use in recipe details text views.
 */
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
