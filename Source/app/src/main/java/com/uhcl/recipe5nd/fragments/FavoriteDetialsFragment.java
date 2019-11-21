package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Ingredient;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Implementation of searching by multiple ingredients. Uses a RecyclerView to display list of
 * user's ingredients. User selects an item to add it to the search parameters.
 */
public class FavoriteDetialsFragment extends Fragment
{
    private static final String TAG = "FragmentFavoriteDetails";

    private ListView IngredientsList;
    private TextView IngredientsInstructions;

    private ArrayList<Ingredient> ingredientsList = new ArrayList<>();
    private String[] ingredientsArray = {"eggs","milk","flour","baking soda","sugar","cinnamon","vanilla"};
    private String ingredientsInstructions = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. \nLorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. \nIt has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. \nIt was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorite_detials, container, false);

        IngredientsList = rootView.findViewById(R.id.recipe_details_ingredient_list);
        ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,ingredientsArray);
        IngredientsList.setAdapter(ingredientsAdapter);

        IngredientsInstructions = rootView.findViewById(R.id.recipe_details_instructions);
        IngredientsInstructions.setText(ingredientsInstructions);


        return rootView;
    }



}
