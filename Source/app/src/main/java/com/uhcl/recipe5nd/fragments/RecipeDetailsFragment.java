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
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class RecipeDetailsFragment extends Fragment implements View.OnClickListener
{
    private Context context;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Constants.currentlyViewedRecipe.getStrMeal());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        context = rootView.getContext();

        ImageView imageView = rootView.findViewById(R.id.recipe_details_image);
        Picasso.get().load(Constants.currentlyViewedRecipe.getStrMealThumb()).into(imageView);

        FloatingActionButton fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setOnClickListener(this);

        TextView recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        String stepsAndMeasures = StringFormatter.formatRecipeIngredientsAndMeasures(Constants.currentlyViewedRecipe);
        recipeIngredientsAndMeasuresText.setText(stepsAndMeasures);

        TextView recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(Constants.currentlyViewedRecipe));

        return rootView;
    }

    private boolean addRecipeToFavorites()
    {
        if (Constants.favoriteRecipes == null)
        {
            Constants.favoriteRecipes = new ArrayList<>();
        }

        Constants.favoriteRecipes.add(Constants.currentlyViewedRecipe);

        String json = CreateJSON.createRecipeJSON(context, Constants.favoriteRecipes, true);
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveFile(json, context, Constants.FAVORITES_FILE_NAME);
    }

    @Override
    public void onClick(View view)
    {
        boolean success = addRecipeToFavorites();

        if (!success)
        {
            String toastText = "Could not save recipe to favorites.";
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        }
        else
        {
            String toastText = "Recipe saved!";
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        }
    }
}

