package com.uhcl.recipe5nd.adapters;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.backgroundTasks.FetchFavoriteImages;
import com.uhcl.recipe5nd.fragments.RecipeDetailsFragment;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

public class FavoriteRecipeAdapter extends RecipeAdapter{
    private static final String TAG = "FavDebugging: ";

    public FavoriteRecipeAdapter(ArrayList<Recipe> recipes) {
        super(recipes);
        new FetchFavoriteImages().execute(getRecipeUrls(recipes));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        holder.bind(position);
        try {
            //cardImage.setImageDrawable(Constants.returnedRecipeImages.get(position));
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                //Switch to the recipe results fragment when a card view is clicked on
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Testing favorite recipes details 1");
                    Constants.currentlyViewedRecipe = Constants.favoriteRecipes.get(position);
                    Constants.currentlyViewedRecipeImage = Constants.favoriteRecipeImages.get(position);

                    Log.d(TAG, "Testing favorite recipes details 2");

                    AppCompatActivity activity = Helper.unwrap(view.getContext());
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("recipe_results")
                            .replace(R.id.fragment_container, new RecipeDetailsFragment())
                            .commit();
                }
            });
            //Log.i(TAG, "onBindViewHolder: size of image array:" + Constants.returnedRecipeImages.size());

        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }

    }

    public String[] getRecipeUrls(ArrayList<Recipe> recipes){
        String[] recipeUrls = new String[recipes.size()];

        for (int i=0; i<recipes.size(); i++) {
            Log.d(TAG, "Recipe URL: "+recipes.get(i).getStrMealThumb());
            recipeUrls[i] = recipes.get(i).getStrMealThumb();
        }

        return recipeUrls;
    }
}
