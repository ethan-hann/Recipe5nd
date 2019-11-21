package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.FavoriteRecipeAdapter;
import com.uhcl.recipe5nd.adapters.RecipeAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewRecipesFragment extends Fragment{

    private static RecyclerView recyclerView;
    private static RecipeAdapter recyclerAdapter;
    private static FileHelper fileHelper;
    private static final String TAG = "FavDebugging: ";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        fileHelper = new FileHelper();
        Log.d(TAG,"Now on fav fragment");
        View rootView = inflater.inflate(R.layout.fragment_view_recipes, container, false);

        recyclerView = rootView.findViewById(R.id.favorite_results_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new FavoriteRecipeAdapter(readRecipesFromFavorites());

        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }

    private ArrayList<Recipe> readRecipesFromFavorites(){

        ArrayList<Recipe> favoriteRecipesList = new ArrayList<Recipe>();

        try{
            String favorites = fileHelper.readFile(getActivity(), Constants.FAVORITES_FILE_NAME);

            Log.d(TAG,"Reading favorites");


            favoriteRecipesList = ParseJSON.parseFavoriteRecipes(favorites);
            Constants.favoriteRecipes = favoriteRecipesList;

            //Log.d(TAG,favorites);
        }catch(Exception e){
            Log.d(TAG,"Favorites JSON not loading.");
            Log.d(TAG,e.getMessage());
        }
        Log.d(TAG,"End of attempt to read favorites JSON");

        return  favoriteRecipesList;
    }
}