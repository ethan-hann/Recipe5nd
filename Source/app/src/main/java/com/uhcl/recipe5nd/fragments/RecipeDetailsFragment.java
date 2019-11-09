package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.RecipeAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeDetailsFragment extends Fragment
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
        recipeNameText = rootView.findViewById(R.id.recipe_name_text);
        recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);

        Recipe r = Constants.currentlyViewedRecipe;

        recipeNameText.setText(r.getStrMeal());
        recipeIngredientsAndMeasuresText.setText(StringFormatter.formatRecipeIngredientsAndMeasures(r));
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(r));

        //recipeDetails.setText(StringFormatter.formatRecipeDetails(Constants.currentlyViewedRecipe));

        return rootView;
    }

}
