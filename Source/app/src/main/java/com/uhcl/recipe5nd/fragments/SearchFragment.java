package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.backgroundTasks.FetchIds;
import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.QueryType;
import com.uhcl.recipe5nd.helperClasses.Recipe;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnName;

import org.json.JSONException;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    private TextView searchProgressText;
    private RecyclerView recyclerView;
    private MaterialButton searchButton;
    private ProgressBar progressBar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Search for Recipes");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        context = getContext();
        helpText = rootView.findViewById(R.id.search_help_text);
        progressBar = rootView.findViewById(R.id.progressBar);
        searchButton = rootView.findViewById(R.id.search_button);
        searchProgressText = rootView.findViewById(R.id.search_progress_text);

        progressBar.setVisibility(View.GONE);
        searchProgressText.setVisibility(View.GONE);
        searchButton.setVisibility(View.VISIBLE);

        //get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initialize data and adapter
        getIngredientsFromPantry(context);
        if (ingredientsList != null)
        {
            Constants.selectedIngredients = new ArrayList<>();
            recyclerAdapter = new SearchIngredientsAdapter(ingredientsList);
            recyclerAdapter.notifyDataSetChanged();

            //set adapter
            recyclerView.setAdapter(recyclerAdapter);

            //set item animator
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }



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

                    new FetchRecipe(this).execute(recipeQueryURLS);

                } else {
                    toastText = "No recipes were found with those ingredients";
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                }

            } catch (MalformedURLException | ExecutionException | InterruptedException e) {
                Log.e(TAG, "onClick: ", e);
            }
        }
    }

    /**
     * Inner class which handles fetching recipes from API
     * A weak reference of the outer-class SearchFragment is obtained to interact
     * with UI elements on the background thread this inner class creates.
     */
    private static class FetchRecipe extends AsyncTask<URL, Integer, ArrayList<Recipe>>
    {
        private static final String TAG = "FetchRecipe";
        private WeakReference<SearchFragment> fragmentWeakReference;

        FetchRecipe(SearchFragment fragment) {
            fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        protected void onPreExecute() {
            SearchFragment f = fragmentWeakReference.get();
            if (f != null && !f.isDetached() && !f.isRemoving())
            {
                f.helpText.setText(R.string.searching_for_recipes);
                f.progressBar.setVisibility(View.VISIBLE);
                f.searchProgressText.setVisibility(View.VISIBLE);
                f.searchButton.setVisibility(View.GONE);
            }
        }

        @Override
        protected ArrayList<Recipe> doInBackground(URL... urls)
        {
            ArrayList<Recipe> recipes = new ArrayList<>();
            try {
                for (int i = 0; i < urls.length; i++)
                {
                    boolean canConnect = APIConnector.executeQuery(urls[i]);
                    if (canConnect)
                    {
                        int progress = (int) (100 * ((double) i / urls.length));
                        //Log.d(TAG, "doInBackground: " + progress);
                        publishProgress(progress);
                        recipes.add(ParseJSON.parseRecipe(APIConnector.apiResponse));
                    }
                }
                return recipes;

            } catch (JSONException e) {
                Log.e(TAG, "doInBackground: ", e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            SearchFragment f = fragmentWeakReference.get();
            if (f != null && !f.isDetached() && !f.isRemoving())
            {
                f.progressBar.setProgress(values[0]);
                f.searchProgressText.setText(String.format(Locale.US, "%d%%", values[0]));
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes)
        {
            super.onPostExecute(recipes);
            SearchFragment f = fragmentWeakReference.get();
            if (f != null && !f.isDetached() && !f.isRemoving())
            {
                Constants.returnedRecipesFromSearch = recipes;
                if (Constants.returnedRecipesFromSearch != null)
                {
                    f.progressBar.setProgress(0);
                    f.progressBar.setVisibility(View.GONE);

                    //Switch to the search results fragment and add to the stack
                    //This allows the use of the back button to return to the SearchFragment
                    try {
                        f.getFragmentManager().beginTransaction()
                                .addToBackStack("searchResults")
                                .replace(R.id.fragment_container, new SearchResultsFragment())
                                .commit();
                    } catch (NullPointerException e) {
                        Log.e(TAG, "onClick: ", e);
                    }

                    f.toastText = String.format(Locale.US, "Search returned %d recipes",
                            Constants.returnedRecipesFromSearch.size());
                    Toast.makeText(f.context, f.toastText, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}