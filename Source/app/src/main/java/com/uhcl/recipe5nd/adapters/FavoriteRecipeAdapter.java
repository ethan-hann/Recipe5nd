package com.uhcl.recipe5nd.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.FavoriteRecipeDetailsFragment;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

public class FavoriteRecipeAdapter extends RecyclerView.Adapter<FavoriteRecipeAdapter.ViewHolder>{
    private static final String TAG = "FavoriteRecipeAdapter: ";
    private Context context;
    private ArrayList<String> imageURLS;

    public FavoriteRecipeAdapter() {
        getRecipeImageURLS();
    }

    private void getRecipeImageURLS()
    {
        imageURLS = new ArrayList<>();
        for (Recipe r : Constants.favoriteRecipes)
        {
            imageURLS.add(r.getStrMealThumb());
        }
    }

    @Override
    public int getItemCount() {
        return Constants.favoriteRecipes == null ? 0 : Constants.favoriteRecipes.size();
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecipeAdapter.ViewHolder holder, int position)
    {
        try {
            holder.bind(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @NonNull
    @Override
    public FavoriteRecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.returned_recipe_card, parent, false);
        context = view.getContext();

        return new FavoriteRecipeAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView cardView;
        ImageView cardImage;
        TextView cardText;

        ViewHolder(@NonNull View view)
        {
            super(view);
            cardView = view.findViewById(R.id.search_results_cardView);
        }

        void bind(int pos) {
            cardImage = cardView.findViewById(R.id.search_results_card_image);
            cardText = cardView.findViewById(R.id.search_results_card_text);
            cardText.setText(Constants.favoriteRecipes.get(pos).getStrMeal());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.currentlyViewedRecipe = Constants.favoriteRecipes.get(pos);

                    AppCompatActivity activity = Helper.unwrap(context);
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("favorite_recipes_details_fragment")
                            .replace(R.id.fragment_container, new FavoriteRecipeDetailsFragment())
                            .commit();
                }
            });

            String url = imageURLS.get(pos);
            Picasso.get().load(url).into(cardImage);
        }
    }
}
