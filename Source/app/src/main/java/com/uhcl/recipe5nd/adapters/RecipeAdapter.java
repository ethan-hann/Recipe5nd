package com.uhcl.recipe5nd.adapters;

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
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

//TODO: Fix card images not loading until user scrolls recyclerview
//TODO: Fix some card images not loading at all
/**
 * Adapater class for the RecyclerView of the SearchResultsFragment
 * This class handles displaying and updating CardViews in the RecyclerView
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>
{
    private static final String TAG = "RecipeAdapter: ";
    private ArrayList<String> imageURLS;


    public RecipeAdapter() {
        if (Constants.returnedRecipesFromSearch == null)
        {
            Constants.returnedRecipesFromSearch = new ArrayList<>();
        }

        if (Constants.returnedRecipeImages == null)
        {
            Constants.returnedRecipeImages = new ArrayList<>();
        }


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
        for (Recipe r : Constants.returnedRecipesFromSearch)
        {
            imageURLS.add(r.getStrMealThumb());
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.returned_recipe_card, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return Constants.returnedRecipesFromSearch == null
                ? 0 : Constants.returnedRecipesFromSearch.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView cardView;
        ImageView cardImage;
        TextView cardText;

        ViewHolder(View view)
        {
            super(view);
            cardView = view.findViewById(R.id.search_results_cardView);
        }

        void bind(int pos)
        {
            cardImage = cardView.findViewById(R.id.search_results_card_image);
            cardText = cardView.findViewById(R.id.search_results_card_text);

            cardText.setText(Constants.returnedRecipesFromSearch.get(pos).getStrMeal());
            
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.currentlyViewedRecipe = Constants.returnedRecipesFromSearch.get(pos);
                    try {
                        Constants.currentlyViewedRecipeImage = Constants.returnedRecipeImages.get(pos);
                    } catch (IndexOutOfBoundsException e) {
                        Log.e(TAG, "onClick: ", e);
                    }

                    AppCompatActivity activity = Helper.unwrap(view.getContext());
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("recipe_details")
                            .replace(R.id.fragment_container, new RecipeDetailsFragment())
                            .commit();
                }
            });

            try {
                cardImage.setImageDrawable(Constants.returnedRecipeImages.get(pos));
            } catch (IndexOutOfBoundsException e) {
                Log.e(TAG, "bind: ", e);
            }
        }
    }
}
