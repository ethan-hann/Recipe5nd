package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.backgroundTasks.FetchIds;
import com.uhcl.recipe5nd.backgroundTasks.FetchRecipe;
import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.QueryType;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnName;

import org.json.JSONException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
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
 * user's ingredients read from a file. User selects an item to add it to the search parameters.
 */
public class SearchFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "SearchFragment";
    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    private String toastText = "";
    private FileHelper fileHelper = new FileHelper();
    private SearchIngredientsAdapter recyclerAdapter;

    private Context context;
    private TextView helpText;
    private RecyclerView recyclerView;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        helpText = rootView.findViewById(R.id.search_help_text);
        context = getContext();

        //get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initialize data and adapter
        getIngredientsFromPantry(context);
        if (ingredientsList != null)
        {
            recyclerAdapter = new SearchIngredientsAdapter(ingredientsList);
            recyclerAdapter.notifyDataSetChanged();

            //set adapter
            recyclerView.setAdapter(recyclerAdapter);

            //set item animator
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        searchButton = rootView.findViewById(R.id.search_button);

        //Search button functionality
        searchButton.setOnClickListener(this);

        return rootView;
    }

    /**
     * Gets the ingredients from a saved ingredients.json file if it exists and is not empty.
     * If it doesn't exist or is empty, a message is displayed at the top of the Fragment.
     * @param context : the application's context
     */
    private void getIngredientsFromPantry(Context context) {
        if (Constants.doesIngredientsFileExist) {
            try {
                String jsonResponse = fileHelper.readFile(context, Constants.INGREDIENTS_FILE_NAME);
                System.out.println(jsonResponse);
                ingredientsList = ParseJSON.parseIngredients(jsonResponse);
                if (ingredientsList == null)
                {
                    helpText.setText(R.string.no_ingredients_file_warning);
                }
                else
                {
                    helpText.setText(R.string.search_help);
                    Collections.sort(ingredientsList, new SortBasedOnName());
                }

            } catch (JSONException e) {
                Log.e(TAG, "getIngredientsFromPantry: ", e);
            }
        }
        else
        {
            helpText.setText(R.string.no_ingredients_file_warning);
        }
    }

    /*
         To look up recipes by their ingredients, we have to look up by both id and ingredients.
         The first API call returns a list of ids that match meals who include the ingredients.
         The second API call returns a list of recipes that match those ids.
         This is a limitation of the API.

         Note that these two calls cannot be run in parallel because the second
         call depends on the result of the first.
    */
    /**
     * OnClick listener for search button
     * @param view : the view that was clicked
     */
    @Override
    public void onClick(View view)
    {
        //ensure at least one ingredient is selected to include in search
        if (!fileHelper.exists(context, Constants.INGREDIENTS_FILE_NAME)
                || ingredientsList == null)
        {
            toastText = "Add some ingredients first in \"Edit Ingredients\"";
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        } else if (Constants.selectedIngredients.isEmpty())
        {
            toastText = "Please select at least one ingredient";
            Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
        } else
        {
            try
            {
                String ingredientQuery = APIConnector.buildQueryString(QueryType.SEARCH_BY_INGREDIENTS, "");
                Log.i(TAG, "idURL: " + ingredientQuery);
                ArrayList<String> ids = new FetchIds().execute(new URL(ingredientQuery)).get();

                if (ids != null) {
                    String[] recipeQueries = new String[ids.size()];
                    for (int i = 0; i < ids.size(); i++) {
                        recipeQueries[i] = APIConnector.buildQueryString(QueryType.SEARCH_BY_ID, ids.get(i));
                    }

                    URL[] recipeQueryURLS = new URL[recipeQueries.length];

                    for (int i = 0; i < recipeQueryURLS.length; i++) {
                        recipeQueryURLS[i] = new URL(recipeQueries[i]);
                    }

                    ArrayList<Recipe> recipes = new FetchRecipe().execute(recipeQueryURLS).get();

                    if (recipes != null) {
                        Constants.returnedRecipesFromSearch = recipes;

                        //Switch to the search results fragment and add to the stack
                        //This allows the use of the back button to return to this Fragment
                        try {
                            getFragmentManager().beginTransaction()
                                    .addToBackStack("searchResults")
                                    .replace(R.id.fragment_container, new SearchResultsFragment())
                                    .commit();
                        } catch (NullPointerException e) {
                            Log.e(TAG, "onClick: ", e);
                        }

                        toastText = String.format(Locale.US, "Search returned %d recipes",
                                recipes.size());
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                    }
                } else {
                    toastText = "No recipes were found with those ingredients";
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                }

            } catch (MalformedURLException | ExecutionException | InterruptedException e) {
                Log.e(TAG, "onClick: ", e);
            }
        }
    }
}
