package com.uhcl.recipe5nd.fragments;

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
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;
import com.uhcl.recipe5nd.helperClasses.QueryType;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import org.json.JSONException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    private TextView helpText;
    private RecyclerView recyclerView;
    private SearchIngredientsAdapter recyclerAdapter;
    private Button searchButton;
    private Button clearButton;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        helpText = rootView.findViewById(R.id.search_help_text);

        //get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initialize data and adapter
        getIngredientsFromPantry();
        recyclerAdapter = new SearchIngredientsAdapter(ingredientsList);
        recyclerAdapter.notifyDataSetChanged();

        //set adapter
        recyclerView.setAdapter(recyclerAdapter);

        //set item animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchButton = rootView.findViewById(R.id.search_button);
        clearButton = rootView.findViewById(R.id.clear_search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ensure at least one ingredient is selected to include in search
                if (!Constants.doesIngredientsFileExist)
                {
                    Toast t = Toast.makeText(getContext(), "No ingredients were found. Please add some in \"Edit Ingredients\".", Toast.LENGTH_LONG);
                    t.show();
                }
                else if (Constants.selectedIngredients.isEmpty()) {
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

                        if (ids != null)
                        {
                            ArrayList<String> recipeQueries = new ArrayList<>();
                            for (int i = 0; i < ids.size(); i++) {
                                recipeQueries.add(APIConnector.buildQueryString(QueryType.SEARCH_BY_ID, ids.get(i)));
                            }

                            URL[] recipeQueryURLS = new URL[recipeQueries.size()];

                            for (int i = 0; i < recipeQueryURLS.length; i++) {
                                recipeQueryURLS[i] = new URL(recipeQueries.get(i));
                            }

                            ArrayList<Recipe> recipes = new FetchRecipe().execute(recipeQueryURLS).get();

                            if (recipes != null)
                            {
                                Log.i(TAG, "Search returned " + recipes.size() + " recipes");
                                Constants.returnedRecipesFromSearch = recipes;

                                //Switch to the search results fragment and add to the stack
                                //This allows the use of the back button to return to this Fragment
                                getFragmentManager().beginTransaction()
                                        .addToBackStack("searchResults")
                                        .replace(R.id.fragment_container, new SearchResultsFragment())
                                        .commit();

                                for (Recipe r : recipes) {
                                    Log.i(TAG, "Recipe Info: " + r.getRecipeInformation());
                                }
                                Toast t = Toast.makeText(getContext(),
                                        String.format(Locale.US, "Found %d recipes",
                                                recipes.size()), Toast.LENGTH_LONG);
                                t.show();
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
                recyclerAdapter.clearSelectedItems();
                System.out.println("CLEAR HAS BEEN CLICKED!");
            }
        });

        return rootView;
    }



    //TODO: implement reading from saved JSON file containing user's ingredients!
    //TODO: TEMPORARY METHOD FOR TESTING
    private void getIngredientsFromPantry() {
        FileHelper fileHelper = new FileHelper();
        ArrayList<Ingredient> testIngredients = new ArrayList<>();
        testIngredients.add(new Ingredient("Chicken", PrimaryTag.HOT));
        testIngredients.add(new Ingredient("Beef", PrimaryTag.COLD, "Meats"));
        String json = CreateJSON.createIngredientsJSON(testIngredients);
        fileHelper.saveFile(json, getContext(), "ingredients.json");

        /*FileHelper fileHelper = new FileHelper();
        Recipe r = new Recipe();
        r.setId("0");
        r.setStrMeal("Spicy Arrabiata Penne");
        r.setStrMealThumb("https://www.themealdb.com/images/media/meals/ustsqw1468250014.jpg");
        r.setStrYoutube("https://www.youtube.com/watch?v=1IszT_guI08");
        r.addIngredient("penne rigate");
        r.addMeasurement("1 pound");
        r.addIngredient("olive oil");
        r.addMeasurement("1/4 cup");
        r.setStrInstructions("Bring a large pot of water to a boil. Add kosher salt to the boiling water, then add the pasta. Cook according to the package instructions, about 9 minutes.\r\nIn a large skillet over medium-high heat, add the olive oil and heat until the oil starts to shimmer. Add the garlic and cook, stirring, until fragrant, 1 to 2 minutes. Add the chopped tomatoes, red chile flakes, Italian seasoning and salt and pepper to taste. Bring to a boil and cook for 5 minutes. Remove from the heat and add the chopped basil.\r\nDrain the pasta and add it to the sauce. Garnish with Parmigiano-Reggiano flakes and more basil and serve warm.");
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(r);
        String json = CreateJSON.createRecipeJSON(recipes);

        fileHelper.saveFile(json, getContext(), "recipes.json");
        //fileHelper.readFile(getContext(), "ingredients.json");*/

        boolean exists = fileHelper.exists(getContext(), "ingredients.json");
        if (exists) {
            try {
                String jsonResponse = fileHelper.readFile(getContext(), "ingredients.json");
                System.out.println(jsonResponse);
                ingredientsList = ParseJSON.parseIngredients(jsonResponse);
                helpText.setText(R.string.search_help);
            } catch (JSONException e) {
                Log.e(TAG, "getIngredientsFromPantry: ", e);
            }
        }
        else
        {
            helpText.setText(R.string.no_ingredients_file_warning);
        }


        /*Ingredient ing1 = new Ingredient("Chicken");
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
        ingredientsList.add(ing16);*/
    }
}
