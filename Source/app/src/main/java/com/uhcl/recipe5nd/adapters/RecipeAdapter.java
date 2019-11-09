package com.uhcl.recipe5nd.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.backgroundTasks.FetchImages;
import com.uhcl.recipe5nd.fragments.RecipeDetailsFragment;
import com.uhcl.recipe5nd.fragments.SearchResultsFragment;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapater class for the RecyclerView of the SearchResultsFragment
 * This class handles displaying and updating CardViews in the RecyclerView
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>
{
    private static final String TAG = "RecipeAdapter: ";
    private ArrayList<Recipe> returnedRecipes;
    private ArrayList<String> imageURLS;
    private ImageView cardImage;
    private TextView cardText;
    private View rootView;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        if (recipes == null) {
            returnedRecipes = new ArrayList<>();
        }
        this.returnedRecipes = recipes;

        Constants.returnedRecipeImages = new ArrayList<>();

        getRecipeImageURLS();
        if (!imageURLS.isEmpty()) 
        {
            String[] urls = new String[imageURLS.size()];
            urls = imageURLS.toArray(urls);
            new FetchImages().execute(urls);
        }
    }

    /**
     * Gets image urls from returned recipes and puts them in an array list
     */
    private void getRecipeImageURLS()
    {
        imageURLS = new ArrayList<>();
        for (Recipe r : returnedRecipes)
        {
            imageURLS.add(r.getStrMealThumb());
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.returned_recipe_card, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        holder.bind(position);
        try {
            cardImage.setImageDrawable(Constants.returnedRecipeImages.get(position));
            holder.cardView.setOnClickListener(new View.OnClickListener() {

                //Switch to the recipe results fragment when a card view is clicked on
                @Override
                public void onClick(View view) {
                    Constants.currentlyViewedRecipe = Constants.returnedRecipesFromSearch.get(position);
                    Constants.currentlyViewedRecipeImage = Constants.returnedRecipeImages.get(position);

                    AppCompatActivity activity = Helper.unwrap(view.getContext());
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("recipe_results")
                            .replace(R.id.fragment_container, new RecipeDetailsFragment())
                            .commit();
                }
            });
            Log.i(TAG, "onBindViewHolder: size of image array:" + Constants.returnedRecipeImages.size());
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (returnedRecipes == null)
        {
            return 0;
        }
        return returnedRecipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView cardView;

        ViewHolder(View view)
        {
            super(view);
            cardView = view.findViewById(R.id.search_results_cardView);
        }

        void bind(int pos) {
            cardImage = cardView.findViewById(R.id.search_results_card_image);
            cardText = cardView.findViewById(R.id.search_results_card_text);

            cardText.setText(returnedRecipes.get(pos).getStrMeal());
        }
    }
}
