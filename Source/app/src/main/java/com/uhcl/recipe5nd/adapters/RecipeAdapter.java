/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Ethan D. Hann
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uhcl.recipe5nd.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.RecipeDetailsFragment;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder>
{
    private static final String TAG = "RecipeAdapter: ";
    private ArrayList<String> imageURLS;


    public RecipeAdapter() {
        if (Global.returnedRecipesFromSearch == null)
        {
            Global.returnedRecipesFromSearch = new ArrayList<>();
        }

        if (Global.returnedRecipeImages == null)
        {
            Global.returnedRecipeImages = new ArrayList<>();
        }

        getRecipeImageURLS();
    }

    /**
     * Gets image urls from returned recipes and puts them in an array list
     */
    private void getRecipeImageURLS()
    {
        imageURLS = new ArrayList<>();
        for (Recipe r : Global.returnedRecipesFromSearch)
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
        return Global.returnedRecipesFromSearch == null
                ? 0 : Global.returnedRecipesFromSearch.size();
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

            cardText.setText(Global.returnedRecipesFromSearch.get(pos).getStrMeal());

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Global.currentlyViewedRecipe = Global.returnedRecipesFromSearch.get(pos);

                    AppCompatActivity activity = Helper.unwrap(view.getContext());
                    activity
                            .getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("recipe_details")
                            .replace(R.id.fragment_container, new RecipeDetailsFragment())
                            .commit();
                }
            });

            //Load images into the card image view using Picasso library
            String url = imageURLS.get(pos);
            Picasso.get().load(url).into(cardImage);
        }
    }
}
