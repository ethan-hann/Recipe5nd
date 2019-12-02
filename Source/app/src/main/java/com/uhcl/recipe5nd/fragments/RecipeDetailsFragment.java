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

package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.RecipeIngredient;
import com.uhcl.recipe5nd.helperClasses.ShoppingList;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnDate;
import com.uhcl.recipe5nd.helperClasses.StringFormatter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class RecipeDetailsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "RecipeDetailsFragment";
    private Context context;
    private View rootView;
    private FileHelper fileHelper = new FileHelper();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipe_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_missing_ingredients)
        {
            boolean success = addMissingIngredientsToShoppingList();
            if (success) {
                String toastText = "Missing ingredients were added to a new Shopping List";
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            }
            else
            {
                String toastText = "Missing ingredients are already in your shopping list";
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
            }
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Global.currentlyViewedRecipe.getStrMeal());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        context = rootView.getContext();

        ImageView imageView = rootView.findViewById(R.id.recipe_details_image);
        Picasso.get().load(Global.currentlyViewedRecipe.getStrMealThumb()).into(imageView);

        FloatingActionButton fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setOnClickListener(this);

        TextView recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        String stepsAndMeasures = StringFormatter.formatRecipeIngredientsAndMeasures(Global.currentlyViewedRecipe);
        recipeIngredientsAndMeasuresText.setText(stepsAndMeasures);

        TextView recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(Global.currentlyViewedRecipe));

        return rootView;
    }

    /**
     * Add a recipe to the favorite recipes file
     * @return true if saving was successful; false if not
     */
    private boolean addRecipeToFavorites()
    {
        if (Global.favoriteRecipes == null)
        {
            Global.favoriteRecipes = new ArrayList<>();
        }

        Global.favoriteRecipes.add(Global.currentlyViewedRecipe);

        String json = CreateJSON.createRecipeJSON(context, Global.favoriteRecipes, true);
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveFile(json, context, Global.FAVORITES_FILE_NAME);
    }

    /**
     * Adds ingredients to a new shopping list if the user did not select them to search with
     * @return true if adding was successful; false if not
     */
    private boolean addMissingIngredientsToShoppingList()
    {
        if (Global.shoppingLists == null)
        {
            Global.shoppingLists = new ArrayList<>();
        }

        Recipe r = Global.currentlyViewedRecipe;

        //Iterate backwards through the ArrayList to facilitate deletion of items
        for (int i = r.getIngredientsAndMeasurements().size() - 1; i > 0; i--)
        {
            RecipeIngredient ri = r.getIngredientsAndMeasurements().get(i);
            String recipeIngredientName = ri.getName();

            for (int j = 0; j < Global.selectedIngredients.size(); j++) {
                String selectedIngredientName = Global.selectedIngredients.get(j).getName();
                if (recipeIngredientName.toLowerCase().equals(selectedIngredientName.toLowerCase())
                        || recipeIngredientName.isEmpty()) {
                    r.getIngredientsAndMeasurements().remove(ri);
                }
            }
        }

        ShoppingList s = new ShoppingList();
        s.setTitle(r.getStrMeal());
        s.setDate(Calendar.getInstance().getTime());

        for (int i = 0; i < r.getIngredientsAndMeasurements().size(); i++) {
            s.addItem(r.getIngredientsAndMeasurements().get(i).getName());
            s.setChecked(i, false);
        }

        getDataFromShoppingList();
        for (int i = 0; i < Global.shoppingLists.size(); i++)
        {
            if (Global.shoppingLists.get(i).getTitle().equals(s.getTitle())) {
                return false;
            }
        }

        if (Global.shoppingLists.contains(s))
        {
            return false;
        }
        else
        {
            Global.shoppingLists.add(s);
            String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
            return fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
        }
    }

    /**
     * Read into Global.shoppingLists the contents of the Shopping List file
     */
    private void getDataFromShoppingList()
    {
        try {
            String shoppingJSON = fileHelper.readFile(context, Global.SHOPPING_LIST_FILE_NAME);
            Global.shoppingLists = ParseJSON.parseShoppingLists(shoppingJSON);

            if (Global.shoppingLists != null)
            {
                Collections.sort(Global.shoppingLists, new SortBasedOnDate());
            }
            else
            {
                Global.shoppingLists = new ArrayList<>();
            }
        } catch (JSONException e) {
            Log.e(TAG, "getIngredientData: ", e);
            Global.shoppingLists = new ArrayList<>();
        }
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

