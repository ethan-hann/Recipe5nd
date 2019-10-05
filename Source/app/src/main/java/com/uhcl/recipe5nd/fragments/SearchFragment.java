package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.helperClasses.Ingredient;

import java.util.ArrayList;

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
    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SearchIngredientsAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        //get a reference to recyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        //set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initialize adapter
        recyclerAdapter = new SearchIngredientsAdapter(ingredientsList);

        //set adapter
        recyclerView.setAdapter(recyclerAdapter);

        //set item animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //initialize data
        getIngredientsFromPantry();
        return rootView;
    }

    //TODO: implement reading from saved ingredients file!
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

        recyclerAdapter.notifyDataSetChanged();
    }
}
