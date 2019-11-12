package com.uhcl.recipe5nd.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.RecipeAdapter;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.backgroundTasks.FetchIds;
import com.uhcl.recipe5nd.backgroundTasks.FetchRecipe;
import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.QueryType;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewRecipesFragment extends Fragment implements View.OnClickListener {
    private Button testRecipes;
    private View rootView;
    private LinearLayout favoriteRecipesCardContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_recipes, container, false);

        //CODE FOR THE BUTTON
        testRecipes = rootView.findViewById(R.id.testRecipesBtn);
        testRecipes.setOnClickListener(this);

        //CODE FOR THE CARD VIEWS


        Recipe recipeHelper = new Recipe();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.testRecipesBtn:
                Log.d("Test: ", "Test recipes button has been clicked.");
                if (Constants.returnedRecipesFromSearch == null) {
                    Log.d("Test: ", "The list of returned recipes is empty");
                } else {

                    if (rootView != null) {
                        favoriteRecipesCardContainer = rootView.findViewById(R.id.favorite_recipes_cardview_container);
                        if (favoriteRecipesCardContainer != null) {
                            for (int i = 0; i < Constants.returnedRecipesFromSearch.size(); i++) {
                                String recipeName = Constants.returnedRecipesFromSearch.get(i).getStrMeal();
                                String recipeThumbnailUrl = Constants.returnedRecipesFromSearch.get(i).getStrMealThumb();
                                favoriteRecipesCardContainer.addView(recipeCardGenerator(recipeName, recipeThumbnailUrl));
                                Log.d("Test: ", "#" + i + " " + Constants.returnedRecipesFromSearch.get(i).getStrMeal());
                            }
                        } else {
                            Log.d("Test: ", "Favorite recipes container is null.");
                        }
                    } else {
                        Log.d("Test: ", "View inflater is null!");
                    }
                }
                break;
        }
    }

    private LinearLayout recipeCardGenerator(String recipeName, String recipeThumbnailUrl) {
        LinearLayout recipeContents = new LinearLayout(getActivity());
        recipeContents.setOrientation(LinearLayout.VERTICAL);

        TextView recipeNameTextView = new TextView(getActivity());
        recipeNameTextView.setText(recipeName);

        ImageView recipeThumbnailImageView = new ImageView(getActivity());
        //recipeThumbnailImageView.setImageDrawable(resolveRecipeImage(recipeThumbnailUrl));
        recipeThumbnailImageView.setImageResource(R.drawable.pie_crust_lattice);

        recipeContents.addView(recipeNameTextView);
        recipeContents.addView(recipeThumbnailImageView);

        return recipeContents;
    }

    private Drawable resolveRecipeImage(String thumbnailURL) {
        try {
            InputStream is = (InputStream) new URL(thumbnailURL).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");

            is.close();
            return d;
        } catch (Exception e) {
            return null;
        }

    }
}