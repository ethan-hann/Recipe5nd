package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import java.util.ArrayList;

public class FavoriteRecipeDetailsFragment extends RecipeDetailsFragment implements View.OnClickListener {
    private static final String TAG = "FavDebugging: ";
    View rootView;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = super.onCreateView(inflater,container,savedInstanceState);
        fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_unfavorite_recipes_24px));
        fab.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.red));
        return rootView;
    }

    @Override
    public void onClick(View view)
    {
        Context context = view.getContext();
/*
        //Delete all recipes that match favorited ID just in case there's more than one
        ArrayList<Recipe> recipesToKeep = new ArrayList<Recipe>();
        int count = 0;
        String currentId = Constants.currentlyViewedRecipe.getId();
        for(int i=0; i<Constants.favoriteRecipes.size(); i++){
            if(!Constants.favoriteRecipes.get(i).getId().equals(currentId)){
                Log.d(TAG,"#"+i+" | Recipes to keep: "+Constants.favoriteRecipes.get(i).getStrMeal());
                recipesToKeep.add(Constants.favoriteRecipes.get(i));
                count++;
            }
        }

        Constants.favoriteRecipes = recipesToKeep;

        for(int i=0; i<Constants.favoriteRecipes.size(); i++){
            Log.d(TAG,"pos#"+i+" | Still in favorite recipes: "+Constants.favoriteRecipes.get(i).getStrMeal());
        }

        new FileHelper().reWriteFavoritesFile(CreateJSON.createRecipeJSON(getContext(),Constants.favoriteRecipes),getContext(),Constants.FAVORITES_FILE_NAME);
*/
        Constants.currentlyViewedRecipe = null;
        Constants.currentlyViewedRecipeImage = null;

        getFragmentManager().popBackStack();
    }
}
