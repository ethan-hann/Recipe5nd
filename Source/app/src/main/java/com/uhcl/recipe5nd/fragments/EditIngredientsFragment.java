package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.IngredientAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class EditIngredientsFragment extends Fragment{
    private static final String TAG = "EditIngredientsFragment";

    // Arrays & Constructors
    private ArrayList<Ingredient> arrayListIngredientObjects = new ArrayList<>();
    private ArrayList<String> arrayListIngredientStrings = new ArrayList<>();
    private FileHelper fileHelper = new FileHelper();
    private ArrayList<Ingredient> temporaryArrayList = new ArrayList<>();
    private IngredientAdapter listViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_edit_ingredients, container, false);

        ListView listView = view.findViewById(R.id.pantryListView);
        Button addButton = view.findViewById(R.id.pantryButton);
        EditText pantryTextBox = view.findViewById(R.id.pantryTextBox);
        EditText pantryOptTag = view.findViewById(R.id.pantryOptionalTag);
        Spinner pantrySpinnerField = view.findViewById(R.id.pantrySpinner);


        //Adapter for the spinner
        ArrayAdapter<PrimaryTag> dataAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, PrimaryTag.values());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pantrySpinnerField.setAdapter(dataAdapter);

        boolean fileExists = fileHelper.exists(getContext(), Constants.INGREDIENTS_FILE_NAME);
        if (fileExists)
        {
            try {
                String ingredientsJSON = fileHelper.readFile(getContext(), Constants.INGREDIENTS_FILE_NAME);
                arrayListIngredientObjects = ParseJSON.parseIngredients(ingredientsJSON);

                if (arrayListIngredientObjects != null) {
                    //Custom ListView data adapter
                    listViewAdapter = new IngredientAdapter(getContext(), arrayListIngredientObjects);
                    listView.setAdapter(listViewAdapter);

                    for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                        if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.HOT)){
                            arrayListIngredientStrings.add(pantryIngredient.getName());
                        }
                    }
                    for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                        if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.ROOM)){
                            arrayListIngredientStrings.add(pantryIngredient.getName());
                        }
                    }
                    for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                        if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.COLD)){
                            arrayListIngredientStrings.add(pantryIngredient.getName());
                        }
                    }
                }
                else
                {
                    arrayListIngredientObjects = new ArrayList<>();
                    listViewAdapter = new IngredientAdapter(getContext(), arrayListIngredientObjects);
                    listView.setAdapter(listViewAdapter);
                }

                if (listViewAdapter != null) {
                    listViewAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                Log.e(TAG, "onCreateView: ", e);
                arrayListIngredientObjects = new ArrayList<>();
            }
        } else
        {
            arrayListIngredientObjects = new ArrayList<>();
        }

        //Ingredient deletion
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Remove the clicked item
            arrayListIngredientObjects.remove(position);
            arrayListIngredientStrings.remove(position);

            String json = CreateJSON.createIngredientsJSON(getContext(), arrayListIngredientObjects, true);
            fileHelper.saveFile(json, getContext(), Constants.INGREDIENTS_FILE_NAME);

            listViewAdapter.notifyDataSetChanged();
        });

        //Adding an ingredient
        addButton.setOnClickListener(v -> {
            String newItem = pantryTextBox.getText().toString();
            PrimaryTag newItemPrimaryTag = (PrimaryTag) pantrySpinnerField.getSelectedItem();
            String newItemOptionalTag = pantryOptTag.getText().toString();
            pantryTextBox.setText("");
            pantrySpinnerField.setSelection(0);
            pantryOptTag.setText("");

            //====================================================================================//
            // Copy data to a new array list to sort the current array list into the proper order //
            //====================================================================================//

            // First check to see if the ingredient with the same name already exists
            if(!arrayListIngredientStrings.contains(newItem)) {
                arrayListIngredientObjects.add(new Ingredient(newItem, newItemPrimaryTag, newItemOptionalTag));
                arrayListIngredientStrings.add(newItem);
                // If not, then make sure the temporary array is clear. If not, clear it
                if (!temporaryArrayList.isEmpty()) {
                    temporaryArrayList.clear();
                }
                // Next copy each ingredient into the temporary array list. Then, clear out the original list.
                else {
                    temporaryArrayList.addAll(arrayListIngredientObjects);
                    arrayListIngredientObjects.clear();

                    // Copy each ingredient into the temporary array list if its primary tag is HOT
                    for (Ingredient h : temporaryArrayList) {
                        if (h.getPrimaryTag().equals(PrimaryTag.HOT)) {
                            arrayListIngredientObjects.add(h);
                        }
                    }

                    // Copy each ingredient into the temporary array list if its primary tag is ROOM
                    for (Ingredient r : temporaryArrayList) {
                        if (r.getPrimaryTag().equals(PrimaryTag.ROOM)) {
                            arrayListIngredientObjects.add(r);
                        }
                    }

                    // Copy each ingredient into the temporary array list if its primary tag is COLD
                    for (Ingredient c : temporaryArrayList) {
                        if (c.getPrimaryTag().equals(PrimaryTag.COLD)) {
                            arrayListIngredientObjects.add(c);
                        }
                    }
                    // Now they are in order with the newly added item.

                    // Next we need to clear out the array list of strings. If they're not in the same order as the objects are it causes problems.
                    arrayListIngredientStrings.clear();

                    // For each object in the ArrayList of objects, get its name and copy it into the array list of Strings.
                    for (Ingredient obj2str : arrayListIngredientObjects) {
                        String objStr = obj2str.getName();
                        arrayListIngredientStrings.add(objStr);
                    }

                    listViewAdapter.notifyDataSetChanged();

                    //Save the file
                    String json = CreateJSON.createIngredientsJSON(getContext(), arrayListIngredientObjects, false);
                    fileHelper.saveFile(json, getContext(), Constants.INGREDIENTS_FILE_NAME);
                }
            }
            // This is the else statement for the original check to see if the item already exists or not. If it does we don't need to do anything.
            else {
                Toast.makeText(getActivity(), "Item already exists in pantry!", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}