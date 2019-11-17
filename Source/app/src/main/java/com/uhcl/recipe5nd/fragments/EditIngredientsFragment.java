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
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
public class EditIngredientsFragment extends Fragment{
    public EditIngredientsFragment() {/* Required empty public constructor*/}

    // Arrays & Constructors
    private ArrayList<Ingredient> arrayListIngredientObjects = new ArrayList<>();
    private ArrayList<String> arrayListIngredientStrings = new ArrayList<>();
    private List<PrimaryTag> spinnerPrimaryTagItemList = new ArrayList<>();
    private FileHelper fileHelper = new FileHelper();
    private ArrayList<Ingredient> temporaryArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

                 //////////////////////////////////////////////////////////////////
                //          TODO:Remove This Block Before Implementation
 //-->    ///////////////////////////////////////////////////////////////////////////////////////////   <--//
         ///     Adding Ingredients Manually    ////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        String TAG = EditIngredientsFragment.class.getSimpleName();
        Log.d(TAG, "Words;");
        arrayListIngredientObjects.add(new Ingredient("Chips (A Room Item)", PrimaryTag.ROOM));
        arrayListIngredientObjects.add(new Ingredient("Beef (A Cold Item)", PrimaryTag.COLD, "Meats"));
        arrayListIngredientObjects.add(new Ingredient("Chicken (A Hot Item)", PrimaryTag.HOT));
        arrayListIngredientObjects.add(new Ingredient("A Room Item", PrimaryTag.ROOM));
        arrayListIngredientObjects.add(new Ingredient("A Cold Item", PrimaryTag.COLD, "Meats"));
        arrayListIngredientObjects.add(new Ingredient("A Hot Item", PrimaryTag.HOT));
//------------>                                                       <----------------------------------//





          ///////////////////////////////////////////////////////////////////////////////////////////
         ///     Identifying Fields    /////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        View view = inflater.inflate(R.layout.edit_ingredients_fragment, container, false);
        ListView listView = view.findViewById(R.id.pantryListView);
        Button addButton = view.findViewById(R.id.pantryButton);
        EditText pantryTextBox = view.findViewById(R.id.pantryTextBox);
        EditText pantryOptTag = view.findViewById(R.id.pantryOptionalTag);
        Spinner pantrySpinnerField = view.findViewById(R.id.pantrySpinner);
        String ingredientsJSON = CreateJSON.createIngredientsJSON(getContext(), arrayListIngredientObjects);

          ///////////////////////////////////////////////////////////////////////////////////////////
         ///     Implementing The Spinner    ///////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        // Select which temperature should be applied to the ingredient
        spinnerPrimaryTagItemList.add(PrimaryTag.HOT);
        spinnerPrimaryTagItemList.add(PrimaryTag.ROOM);
        spinnerPrimaryTagItemList.add(PrimaryTag.COLD);

        // Adapter for the spinner
        ArrayAdapter<PrimaryTag> dataAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, spinnerPrimaryTagItemList);

        // Set these options as dropdown options for the spinner
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner adapter call
        pantrySpinnerField.setAdapter(dataAdapter);

          ////////////////////////////////////////////////////////////////////////////////////////////
         ///        ListView Adapter        /////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////

        // Array adapter to tell list view what to display
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayListIngredientStrings);

        // Set the adapter
        listView.setAdapter(listViewAdapter);

          //////////////////////////////////////////////////////////////////////////////////////////
         ///     Populating The List     //////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////

        boolean jsonExists = fileHelper.exists(Objects.requireNonNull(getContext()), Constants.INGREDIENTS_FILE_NAME);
        if (jsonExists) {
            for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.HOT)){
                    String pantryIngredientString = pantryIngredient.getName();
                    arrayListIngredientStrings.add(pantryIngredientString);
                }
            }
            for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.ROOM)){
                    String pantryIngredientString = pantryIngredient.getName();
                    arrayListIngredientStrings.add(pantryIngredientString);
                }
            }
            for (Ingredient pantryIngredient : arrayListIngredientObjects) {
                if(pantryIngredient.getPrimaryTag().equals(PrimaryTag.COLD)){
                    String pantryIngredientString = pantryIngredient.getName();
                    arrayListIngredientStrings.add(pantryIngredientString);
                }
            }
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getActivity(), "Ingredients.json does not exist", Toast.LENGTH_LONG).show();
        }

          //////////////////////////////////////////////////////////////////////////////////////////
         ///  When a listView Item is Clicked -> implementing ingredient deletion  ////////////////
        //////////////////////////////////////////////////////////////////////////////////////////

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Remove the clicked item
            arrayListIngredientObjects.remove(position);
            arrayListIngredientStrings.remove(position);

            fileHelper.saveFile(ingredientsJSON, getContext(), Constants.INGREDIENTS_FILE_NAME);
            listViewAdapter.notifyDataSetChanged();
        }); // end setOnItemClickListener

          //////////////////////////////////////////////////////////////////////////////////////////
         /// When the ADD addButton is clicked ////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////////////////////////

        addButton.setOnClickListener(v -> {
            String newItem = pantryTextBox.getText().toString();
            PrimaryTag newItemPrimaryTag = (PrimaryTag) pantrySpinnerField.getSelectedItem();
            String newItemOptionalTag = pantryOptTag.getText().toString();

            // Add the item to the list of ingredients

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
                    // Save that boiiiii
                    fileHelper.saveFile(ingredientsJSON, getContext(), Constants.INGREDIENTS_FILE_NAME);
                }
            }
            // This is the else statement for the original check to see if the item already exists or not. If it does we don't need to do anything.
            else{
                Toast.makeText(getActivity(), "Item already exists in pantry!", Toast.LENGTH_LONG).show();
            }

            // FINALLY we refresh adapter
            listViewAdapter.notifyDataSetChanged();
        }); //end setOnClickListener
        return view;
    } // end onCreateView
} // end class