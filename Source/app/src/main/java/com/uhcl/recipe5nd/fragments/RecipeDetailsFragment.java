package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.RecipeAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import java.util.ArrayList;

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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean success = addRecipeToFavorites(getContext());

                if (!success)
                {
                    Toast t = Toast.makeText(getContext(), "Could not save recipe to favorites.", Toast.LENGTH_LONG);
                    t.show();
                }
                else
                {
                    Toast t = Toast.makeText(getContext(), "Recipe Saved!", Toast.LENGTH_LONG);
                    t.show();
                }
            }
        });

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

    private boolean addRecipeToFavorites(Context context)
    {
        if (Constants.favoriteRecipes == null)
        {
            Constants.favoriteRecipes = new ArrayList<>();
        }
        Constants.favoriteRecipes.add(Constants.currentlyViewedRecipe);

        String json = CreateJSON.createRecipeJSON(Constants.favoriteRecipes);
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveFile(json, context, "recipes.json");
    }
}
