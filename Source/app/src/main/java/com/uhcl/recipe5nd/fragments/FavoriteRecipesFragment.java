/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Thu Le
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
import android.view.View;
import android.view.ViewGroup;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.FavoriteRecipeAdapter;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;

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
    private FileHelper fileHelper = new FileHelper();
    private Context context;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Favorite Recipes: " +
                ((Global.favoriteRecipes == null) ? 0 : Global.favoriteRecipes.size()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_recipes, container, false);
        context = getContext();

        recyclerView = rootView.findViewById(R.id.favorite_results_recycler_view);

        getFavoriteRecipes(context);
        if (Global.favoriteRecipes != null) {
            recyclerAdapter = new FavoriteRecipeAdapter();
            recyclerAdapter.notifyDataSetChanged();

            recyclerView.setAdapter(recyclerAdapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return rootView;
    }

    private void getFavoriteRecipes(Context context) {
        if (Global.doesFavoritesExist) {
            try {
                String jsonResponse = fileHelper.readFile(context, Global.FAVORITES_FILE_NAME);
                Global.favoriteRecipes = ParseJSON.parseLocalRecipes(jsonResponse);
                if (Global.favoriteRecipes == null)
                {
                    Global.favoriteRecipes = new ArrayList<>();
                }

            } catch (JSONException e) {
                Log.e(TAG, "getIngredientsFromPantry: ", e);
            }
        }
    }
}