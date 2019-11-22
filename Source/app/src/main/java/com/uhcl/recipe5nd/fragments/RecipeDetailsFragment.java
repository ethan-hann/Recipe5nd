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
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.Recipe;
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(Constants.currentlyViewedRecipe.getStrMeal());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        context = rootView.getContext();

        ImageView imageView = rootView.findViewById(R.id.recipe_details_image);
        Picasso.get().load(Constants.currentlyViewedRecipe.getStrMealThumb()).into(imageView);

        FloatingActionButton fab = rootView.findViewById(R.id.favorite_recipe_fab);
        fab.setOnClickListener(this);

        TextView recipeIngredientsAndMeasuresText = rootView.findViewById(R.id.recipe_ingredients_and_measures_text);
        String stepsAndMeasures = StringFormatter.formatRecipeIngredientsAndMeasures(Constants.currentlyViewedRecipe);
        recipeIngredientsAndMeasuresText.setText(stepsAndMeasures);

        TextView recipeStepsText = rootView.findViewById(R.id.recipe_steps_text);
        recipeStepsText.setText(StringFormatter.formatRecipeSteps(Constants.currentlyViewedRecipe));

        return rootView;
    }

    /**
     * Add a recipe to the favorite recipes file
     * @return true if saving was successful; false if not
     */
    private boolean addRecipeToFavorites()
    {
        if (Constants.favoriteRecipes == null)
        {
            Constants.favoriteRecipes = new ArrayList<>();
        }

        Constants.favoriteRecipes.add(Constants.currentlyViewedRecipe);

        String json = CreateJSON.createRecipeJSON(context, Constants.favoriteRecipes, true);
        FileHelper fileHelper = new FileHelper();
        return fileHelper.saveFile(json, context, Constants.FAVORITES_FILE_NAME);
    }

    /**
     * Adds ingredients to a new shopping list if the user did not select them to search with
     * @return true if adding was successful; false if not
     */
    private boolean addMissingIngredientsToShoppingList()
    {
        if (Constants.shoppingLists == null)
        {
            Constants.shoppingLists = new ArrayList<>();
        }

        Recipe r = Constants.currentlyViewedRecipe;
        ArrayList<Ingredient> missingIngredients = new ArrayList<>();

        for (int i = 0; i < r.getIngredientsAndMeasurements().size(); i++)
        {
            for (int j = 0; j < Constants.selectedIngredients.size(); j++)
            {
                String ing = r.getIngredientsAndMeasurements().get(i).getName();
                String selectedIng = Constants.selectedIngredients.get(j).getName();

                Ingredient ingredient = new Ingredient(ing);

                if (!ing.toLowerCase().equals(selectedIng.toLowerCase())
                        && !missingIngredients.contains(ingredient))
                {
                    missingIngredients.add(ingredient);
                }
            }
        }
        ShoppingList s = new ShoppingList();
        s.setTitle(Constants.currentlyViewedRecipe.getStrMeal());
        s.setDate(Calendar.getInstance().getTime());

        for (Ingredient i : missingIngredients) {
            s.addItem(i.getName());
        }

        getDataFromShoppingList();
        for (int i = 0; i < Constants.shoppingLists.size(); i++)
        {
            if (Constants.shoppingLists.get(i).getTitle().equals(s.getTitle())) {
                return false;
            }
        }

        if (Constants.shoppingLists.contains(s))
        {
            return false;
        }
        else
        {
            Constants.shoppingLists.add(s);
            String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
            return fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
        }
    }

    /**
     * Read into Constants.shoppingLists the contents of the Shopping List file
     */
    private void getDataFromShoppingList()
    {
        try {
            String shoppingJSON = fileHelper.readFile(context, Constants.SHOPPING_LIST_FILE_NAME);
            Constants.shoppingLists = ParseJSON.parseShoppingLists(shoppingJSON);

            if (Constants.shoppingLists != null)
            {
                Collections.sort(Constants.shoppingLists, new SortBasedOnDate());
            }
            else
            {
                Constants.shoppingLists = new ArrayList<>();
            }
        } catch (JSONException e) {
            Log.e(TAG, "getIngredientData: ", e);
            Constants.shoppingLists = new ArrayList<>();
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

