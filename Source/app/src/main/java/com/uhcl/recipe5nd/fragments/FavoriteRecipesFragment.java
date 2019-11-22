package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.FavoriteRecipeAdapter;
import com.uhcl.recipe5nd.backgroundTasks.FetchFavoriteImages;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class FavoriteRecipesFragment extends Fragment {

    private static final String TAG = "FavRecipesFragment: ";
    private RecyclerView recyclerView;
    private FavoriteRecipeAdapter recyclerAdapter;
    private ArrayList<Recipe> favoriteRecipes;
    private FileHelper fileHelper = new FileHelper();
    private Context context;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorite Recipes: " +
                ((Constants.favoriteRecipes == null) ? 0 : Constants.favoriteRecipes.size()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_recipes, container, false);
        context = getContext();

        recyclerView = rootView.findViewById(R.id.favorite_results_recycler_view);

        getFavoriteRecipes(context);
        if (Constants.favoriteRecipes != null) {
            recyclerAdapter = new FavoriteRecipeAdapter();
            recyclerAdapter.notifyDataSetChanged();

            recyclerView.setAdapter(recyclerAdapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return rootView;
    }

    private void getFavoriteRecipes(Context context) {
        if (Constants.doesFavoritesExist) {
            try {
                String jsonResponse = fileHelper.readFile(context, Constants.FAVORITES_FILE_NAME);
                Log.d(TAG, "getFavoriteRecipes: " + jsonResponse);
                Constants.favoriteRecipes = ParseJSON.parseLocalRecipes(jsonResponse);
                if (Constants.favoriteRecipes != null)
                {
                    String[] urls = new String[Constants.favoriteRecipes.size()];
                    for (int i = 0; i < urls.length; i++) {
                        urls[i] = Constants.favoriteRecipes.get(i).getStrMealThumb();
                    }
                    new FetchFavoriteImages().execute(urls);
                }
                else
                {
                    Constants.favoriteRecipes = new ArrayList<>();
                    Constants.favoriteRecipeImages = new ArrayList<>();
                }

            } catch (JSONException e) {
                Log.e(TAG, "getIngredientsFromPantry: ", e);
            }
        }
    }
}