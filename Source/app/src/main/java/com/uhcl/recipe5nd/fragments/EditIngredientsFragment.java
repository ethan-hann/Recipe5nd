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
    public EditIngredientsFragment() {/* Required empty public constructor*/}
    public static EditIngredientsFragment newInstance() {EditIngredientsFragment fragment = new EditIngredientsFragment();return fragment;}

    private EditText foodItem;
    private EditText optTag;
    private int item;
    private String jsonResponse;

    // Start //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // To Identify Items using view.findViewByID
        View view = inflater.inflate(R.layout.edit_ingredients_fragment, container, false);

        // Arrays
        ArrayList<String> foods = new ArrayList<String>();
        ArrayList<Ingredient> pantryIngredient = new ArrayList<>();

        // Constructors
        FileHelper fileHelper = new FileHelper();

        // identify fields
        ListView listView = (ListView) view.findViewById(R.id.pantryListView);
        Button button = (Button) view.findViewById(R.id.pantryButton);
        foodItem = (EditText) view.findViewById(R.id.pantryTextBox);
        optTag = (EditText) view.findViewById(R.id.pantryOptionalTag);
        Spinner pantrySpinner = view.findViewById(R.id.pantrySpinner);

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
        listView.setAdapter(listViewAdapter);




          // It breaks without this
         //But doesnt read ingredients.json??????????
////////////////////////////////////////////////////////////////////////////////
        //check if ingredients.json exists
        String ingredientsFile = "ingredients.json";
        String jsonResponse = fileHelper.readFile(getContext(), ingredientsFile);
        boolean jsonExists = fileHelper.exists(getContext(), ingredientsFile);
        if(jsonExists) {
            try {
                pantryIngredient = ParseJSON.parseIngredients(jsonResponse);
            }catch (JSONException e) {e.printStackTrace();} //end catch

        } //end if
        else{Toast.makeText(getActivity(), "Nope", Toast.LENGTH_LONG).show();}
//////////////////////////////////////////////////////////////////////////////


                    //Sort the listView Items by Hot, Room, then Cold
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
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
                if(primaryTags.equals("ROOM")){
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
        }catch (JSONException e) {e.printStackTrace();}

        // Refresh the adapter after running the sort by temperature
        listViewAdapter.notifyDataSetChanged();

        // When a listView Item is Clicked -> implementing ingredient deletion
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == position){
                   int intID = l2i(id);
                    try{
                        JSONArray jsonArray = new JSONArray(jsonResponse);
                        for(int i = 0, size = jsonArray.length(); i<size; i++) {
                            JSONObject thisJsonObject = jsonArray.getJSONObject(i);
                            String ingredientNames = thisJsonObject.getString("name");
                            String primaryTags = thisJsonObject.getString("primaryTag");
                            String optionalTags = thisJsonObject.getString("optionalTag");

                            // Expecting the first 3 IDs (0, 1, 2) to be the category labels for hot, room, and cold.
                            // If this is not implemented, just need to delete this outer if statement and keep the inner one.
                            if(intID > 2){
                                if (i == intID) {
                                    foods.remove(ingredientNames);
                                    foods.remove(primaryTags);
                                    foods.remove(optionalTags);
                                    listViewAdapter.notifyDataSetChanged();
                                }
                            }else{
                                Toast.makeText(getActivity(), "Unable to remove category item!", Toast.LENGTH_LONG).show();
                            }

                        }
                    }catch(JSONException e){e.printStackTrace();}
                }
            }
        }); // end setOnItemClickListener


        // onClick for the ADD Button
        ArrayList<Ingredient> finalPantryIngredient = pantryIngredient;
        button.setOnClickListener(v -> {Toast.makeText(getActivity(), "Added to pantry", Toast.LENGTH_SHORT).show();

            //Make both EditTexts values into type String
            String foodString = foodItem.getText().toString();
            String optString = optTag.getText().toString();

            //add the food String to the arrayList of food Strings to update the listView
            foods.add(foodString);

            //add the items to the ArrayList of <Ingredient>
            if (finalPantryIngredient != null) {
                finalPantryIngredient.add(new Ingredient(foodString, (PrimaryTag) pantrySpinner.getSelectedItem(), optString));
            }

            // After clicking the add button, the last thing to do is update the listView again to display the new item
            listViewAdapter.notifyDataSetChanged();
        }); // end setOnClickListener

        return view;
    } // end onCreateView

    public static int l2i(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");}
        return (int) l;
    }
} // end class