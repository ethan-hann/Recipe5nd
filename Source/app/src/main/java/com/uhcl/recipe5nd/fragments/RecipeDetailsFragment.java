package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeDetailsFragment extends Fragment implements View.OnClickListener
{
    private ImageView imageView;
    private TextView recipeNameText;
    private TextView recipeIngredientsAndMeasuresText;
    private TextView recipeStepsText;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        imageView = rootView.findViewById(R.id.recipe_details_image);
        imageView.setImageDrawable(Constants.currentlyViewedRecipeImage);

        fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setOnClickListener(this);

        Recipe r = Constants.currentlyViewedRecipe;
        recipeNameText = rootView.findViewById(R.id.recipe_name_text);
        recipeNameText.setText(r.getStrMeal());

        recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        recipeIngredientsAndMeasuresText.setText(StringFormatter.formatRecipeIngredientsAndMeasures(r));

        recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(r));

        return rootView;
    }

    private boolean addRecipeToFavorites(Context context)
    {
        if (Constants.favoriteRecipes == null)
        {
            Constants.favoriteRecipes = new ArrayList<>();
        }

        if (Constants.favoriteRecipeImages == null)
        {
            Constants.favoriteRecipeImages = new ArrayList<>();
        }

        Constants.favoriteRecipes.add(Constants.currentlyViewedRecipe);
        Constants.favoriteRecipeImages.add(Constants.currentlyViewedRecipeImage);

        String json = CreateJSON.createRecipeJSON(context, Constants.favoriteRecipes, false);
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveFile(json, context, Constants.FAVORITES_FILE_NAME);
    }

    @Override
    public void onClick(View view)
    {
        Context context = view.getContext();
        boolean success = addRecipeToFavorites(context);

        if (!success)
        {
            Toast t = Toast.makeText(context, "Could not save recipe to favorites.", Toast.LENGTH_LONG);
            t.show();
        }
        else
        {
            Toast t = Toast.makeText(context, "Recipe Saved!", Toast.LENGTH_LONG);
            t.show();
        }
    }
}

