/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Ogheneovo (Nova) Abu
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

package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

public class FavoriteRecipeDetailsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "FavRecipeDetailsFrag: ";
    private FileHelper fileHelper = new FileHelper();
    private Context context;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(
                Global.currentlyViewedRecipe.getStrMeal());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        context = rootView.getContext();

        ImageView imageView = rootView.findViewById(R.id.recipe_details_image);
        Picasso.get().load(Global.currentlyViewedRecipe.getStrMealThumb()).into(imageView);

        FloatingActionButton fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setOnClickListener(this);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfavorite_recipes_24px));

        TextView recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        String stepsAndMeasures = StringFormatter.formatRecipeIngredientsAndMeasures(Global.currentlyViewedRecipe);
        recipeIngredientsAndMeasuresText.setText(stepsAndMeasures);

        TextView recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(Global.currentlyViewedRecipe));
        return rootView;
    }

    private boolean removeRecipeFromFavorites()
    {
        if (Global.favoriteRecipes != null) {
            Global.favoriteRecipes.remove(Global.currentlyViewedRecipe);
            Global.currentlyViewedRecipe = null;

            String json = CreateJSON.createRecipeJSON(context, Global.favoriteRecipes, true);
            fileHelper.saveFile(json, context, Global.FAVORITES_FILE_NAME);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view)
    {
        boolean success = removeRecipeFromFavorites();

        if (!success) {
            String toastText = "Could not remove recipe from favorites.";
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        }
        else
        {
            try {
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (NullPointerException e) {
                Log.e(TAG, "onClick: ", e);
            }
        }
    }
}
