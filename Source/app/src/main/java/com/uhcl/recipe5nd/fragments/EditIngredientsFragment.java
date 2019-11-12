package com.uhcl.recipe5nd.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.SearchIngredientsAdapter;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.zip.Inflater;
public class EditIngredientsFragment extends Fragment{

    private EditText foodItem;
    private EditText optTag;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditIngredientsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditIngredientsFragment newInstance(String param1, String param2) {
        EditIngredientsFragment fragment = new EditIngredientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    //Generated, untouched
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // To Identify Items using view.findViewByID
        View view = inflater.inflate(R.layout.edit_ingredients_fragment, container, false);

        // Arrays
        ArrayList<String> foods = new ArrayList<String>();
        ArrayList<Ingredient> pantryIngredient = new ArrayList<>();

        // Constructors
        FileHelper fileHelper = new FileHelper();

        //Strings
        //String optTag = "";

        // identify fields
        ListView listView = (ListView) view.findViewById(R.id.pantryListView);
        Button button = (Button) view.findViewById(R.id.pantryButton);
        foodItem = (EditText) view.findViewById(R.id.pantryTextBox);
        optTag = (EditText) view.findViewById(R.id.pantryOptionalTag);
        Spinner pantrySpinner = (Spinner) view.findViewById(R.id.pantrySpinner);

        // --# Spinner #-- //
        // select which temperature should be applied to the ingredient
        List<PrimaryTag> spinnerList = new ArrayList<>();
        spinnerList.add(PrimaryTag.HOT);
        spinnerList.add(PrimaryTag.ROOM);
        spinnerList.add(PrimaryTag.COLD);
        //adapter for the spinner
        ArrayAdapter<PrimaryTag> dataAdapter = new ArrayAdapter<PrimaryTag>(getActivity(), android.R.layout.simple_spinner_item, spinnerList);
        // set these options as dropdown options for the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinner adapter call
        pantrySpinner.setAdapter(dataAdapter);
        // array adapter to tell list view what to display
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, foods);

        // check if ingredients.json exists
        String ingredientsFile = "ingredients.json";
        String jsonResponse = fileHelper.readFile(getContext(), ingredientsFile);
        boolean jsonExists = fileHelper.exists(getContext(), ingredientsFile);
        if(jsonExists) {
            try {
                pantryIngredient = ParseJSON.parseIngredients(jsonResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            } //foods.add(jsonResponse);
            // just need to split this response up to return each food individually then place into a for loop
        }

        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
                    //JSONObject jsonObject = jsonArray.getJSONObject(0);
                    //String ingredientName = jsonObject.getString("name");
                    //String primaryTag = jsonObject.getString("primaryTag");
                    //String optionalTag = jsonObject.getString("optionalTag");
            for(int i = 0, size = jsonArray.length(); i<size; i++){
                JSONObject thisJsonObject = jsonArray.getJSONObject(i);
                String ingredientNames = thisJsonObject.getString("name");
                String primaryTags = thisJsonObject.getString("primaryTag");
                String optionalTags = thisJsonObject.getString("optionalTag");
                // Display hot items
                if(primaryTags.equals("HOT")){
                    foods.add(ingredientNames);
                }
            }

            for(int i = 0, size = jsonArray.length(); i<size; i++){
                JSONObject thisJsonObject = jsonArray.getJSONObject(i);
                String ingredientNames = thisJsonObject.getString("name");
                String primaryTags = thisJsonObject.getString("primaryTag");
                String optionalTags = thisJsonObject.getString("optionalTag");
                // Display warm items
                if(primaryTags.equals("WARM")){
                    foods.add(ingredientNames);

                }
            }

            for(int i = 0, size = jsonArray.length(); i<size; i++){
                JSONObject thisJsonObject = jsonArray.getJSONObject(i);
                String ingredientNames = thisJsonObject.getString("name");
                String primaryTags = thisJsonObject.getString("primaryTag");
                String optionalTags = thisJsonObject.getString("optionalTag");
                // Display cold items
                if(primaryTags.equals("COLD")){
                    foods.add(ingredientNames);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listView.setAdapter(listViewAdapter);
        // For the items in the listView, when the item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == position){
                    Toast.makeText(getActivity(), "Prompt blah blah blah", Toast.LENGTH_SHORT).show();
                } //end if
            } //end onItemClick
        }); // end setOnItemClickListener


        // When add button is clicked
        ArrayList<Ingredient> finalPantryIngredient = pantryIngredient;
        button.setOnClickListener(v -> {
            Toast.makeText(getActivity(), "Added to pantry", Toast.LENGTH_SHORT).show();
            // add the item to the listView
            //make the food a string
            String foodString = foodItem.getText().toString();
            String optString = optTag.getText().toString();
            //add it to foods
            foods.add(foodString);
            // this makes it update the list on screen after the add button is clicked
            finalPantryIngredient.add(new Ingredient(foodString, (PrimaryTag) pantrySpinner.getSelectedItem(), optString));
            listView.setAdapter(listViewAdapter);
        }); // end setOnClickListener

        return view;
    } // end onCreateView
} // end class