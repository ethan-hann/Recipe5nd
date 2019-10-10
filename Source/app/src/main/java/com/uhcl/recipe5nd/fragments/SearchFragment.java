package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.backgroundTasks.FetchIds;
import com.uhcl.recipe5nd.backgroundTasks.FetchRecipe;
import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.QueryType;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Implementation of searching by multiple ingredients. Uses a RecyclerView to display list of
 * user's ingredients. User selects an item to add it to the search parameters.
 */
public class SearchFragment extends Fragment
{
    private static final String TAG = "SearchFragment";
    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();

    private RecyclerView recyclerView;
    private SearchIngredientsAdapter recyclerAdapter;
    private Button searchButton;
    private Button clearButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //initialize data
        getIngredientsFromPantry();

        //get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initialize adapter
        recyclerAdapter = new SearchIngredientsAdapter(ingredientsList);

        //set adapter
        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

        //set item animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchButton = rootView.findViewById(R.id.search_button);
        clearButton = rootView.findViewById(R.id.clear_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ensure at least one ingredient is selected to include in search
                if (Constants.selectedIngredients.isEmpty()) {
                    Toast t = Toast.makeText(getContext(), "Please select at least one ingredient", Toast.LENGTH_LONG);
                    t.show();
                }
                else
                {
                    try
                    {
                        /*
                         To look up recipes by their ingredients, we have to look up by both id and ingredients.
                         The first API call returns a list of ids that match meals who include the ingredients.
                         The second API call returns a list of recipes that match those ids.

                         Note that these two calls cannot be run in parallel because the second
                         call depends on the result of the first.
                         */
                        String ingredientQuery = APIConnector.buildQueryString(QueryType.SEARCH_BY_INGREDIENTS, "");
                        Log.i(TAG, "idURL: " + ingredientQuery);
                        ArrayList<String> ids = new FetchIds().execute(new URL(ingredientQuery)).get();

                        if (ids != null) {
                            ArrayList<String> recipeQueries = new ArrayList<>();
                            for (int i = 0; i < ids.size(); i++) {
                                recipeQueries.add(APIConnector.buildQueryString(QueryType.SEARCH_BY_ID, ids.get(i)));
                            }

                            URL[] recipeQueryURLS = new URL[recipeQueries.size()];

                            for (int i = 0; i < recipeQueryURLS.length; i++) {
                                recipeQueryURLS[i] = new URL(recipeQueries.get(i));
                            }

                            ArrayList<Recipe> recipes = new FetchRecipe().execute(recipeQueryURLS).get();

                            if (recipes != null) {
                                Log.i(TAG, "Search returned " + recipes.size() + " recipes");
                                for (Recipe r : recipes) {
                                    Log.i(TAG, "Recipe Info: " + r.getRecipeInformation());
                                }
                                Toast t = Toast.makeText(getContext(),
                                        String.format(Locale.US, "Found %d recipes",
                                                recipes.size()), Toast.LENGTH_LONG);
                                t.show();
                            }
                            else
                            {
                                Log.i(TAG, "No recipes found");
                            }
                        }
                        else
                        {
                            Toast t = Toast.makeText(getContext(), "No recipes were found with those ingredients", Toast.LENGTH_LONG);
                            t.show();
                        }

                    } catch (MalformedURLException | ExecutionException | InterruptedException e)
                    {
                        Log.e(TAG, "onClick: ", e);
                    }
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.selectedIngredients = new ArrayList<>();
                recyclerAdapter.notifyDataSetChanged();
                System.out.println("CLEAR HAS BEEN CLICKED!");//TODO: implement clearing
            }
        });

        return rootView;
    }



    //TODO: implement reading from saved JSON file containing user's ingredients!
    //TODO: TEMPORARY METHOD FOR TESTING
    private void getIngredientsFromPantry() {
        Ingredient ing1 = new Ingredient("Chicken");
        Ingredient ing2 = new Ingredient("Beef");
        Ingredient ing3 = new Ingredient("Salmon");
        Ingredient ing4 = new Ingredient("Salt");
        Ingredient ing5 = new Ingredient("Kale");
        Ingredient ing6 = new Ingredient("Jasmine Rice");
        Ingredient ing7 = new Ingredient("Jalapeno");
        Ingredient ing8 = new Ingredient("Lamb");
        Ingredient ing9 = new Ingredient("Ground Almonds");
        Ingredient ing10 = new Ingredient("Green Salsa");
        Ingredient ing11 = new Ingredient("Ginger Paste");
        Ingredient ing12 = new Ingredient("Lemon Juice");
        Ingredient ing13 = new Ingredient("Lemons");
        Ingredient ing14 = new Ingredient("Macaroni");
        Ingredient ing15 = new Ingredient("Milk");
        Ingredient ing16 = new Ingredient("Potatoes");

        ingredientsList.add(ing1);
        ingredientsList.add(ing2);
        ingredientsList.add(ing3);
        ingredientsList.add(ing4);
        ingredientsList.add(ing5);
        ingredientsList.add(ing6);
        ingredientsList.add(ing7);
        ingredientsList.add(ing8);
        ingredientsList.add(ing9);
        ingredientsList.add(ing10);
        ingredientsList.add(ing11);
        ingredientsList.add(ing12);
        ingredientsList.add(ing13);
        ingredientsList.add(ing14);
        ingredientsList.add(ing15);
        ingredientsList.add(ing16);
    }
}
