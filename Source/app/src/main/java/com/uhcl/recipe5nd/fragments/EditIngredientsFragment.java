package com.uhcl.recipe5nd.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.IngredientAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnTag;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class EditIngredientsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "EditIngredientsFragment";
    private FileHelper fileHelper = new FileHelper();
    private IngredientAdapter listViewAdapter;
    private Context context;
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_ingredients, container, false);
        context = getContext();

        addButton = rootView.findViewById(R.id.ingredient_add_button);
        addButton.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.pantryRecyclerView);

        getIngredientData(context);
        if (Constants.usersIngredients != null) {
            listViewAdapter = new IngredientAdapter();
            listViewAdapter.notifyDataSetChanged();

            recyclerView.setAdapter(listViewAdapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe());
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return rootView;
    }

    private ItemTouchHelper.SimpleCallback swipe()
    {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Constants.usersIngredients.remove(position);
                Collections.sort(Constants.usersIngredients, new SortBasedOnTag());
                String json = CreateJSON.createIngredientsJSON(context, Constants.usersIngredients, true);
                fileHelper.saveFile(json, context, Constants.INGREDIENTS_FILE_NAME);
                listViewAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    public void onClick(View view)
    {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.ingredient_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        AlertDialog dialog = dialogBuilder.create();

        TextView textView = dialogView.findViewById(R.id.ingredient_dialog_text_view);
        textView.setText(getString(R.string.add_a_new_ingredient));

        EditText ingNameTextBox = dialogView.findViewById(R.id.ingredient_dialog_edit_name);
        EditText ingOptionalTagBox = dialogView.findViewById(R.id.ingredient_dialog_edit_opt_tag);
        Spinner primaryTagSpinner = dialogView.findViewById(R.id.ingredient_dialog_spinner);
        ArrayAdapter<PrimaryTag> spinnerAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, PrimaryTag.values());
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primaryTagSpinner.setAdapter(spinnerAdapter);

        MaterialButton okButton = dialogView.findViewById(R.id.ingredient_dialog_ok);
        MaterialButton cancelButton = dialogView.findViewById(R.id.ingredient_dialog_cancel);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ingredient i = new Ingredient();
                String ingredientName = ingNameTextBox.getText().toString();
                String optionalTag = ingOptionalTagBox.getText().toString();
                PrimaryTag primaryTag = (PrimaryTag) primaryTagSpinner.getSelectedItem();
                boolean validInput = Helper.validateInput(ingredientName);
                boolean ingredientExists = false;

                for (int j = 0; j < Constants.usersIngredients.size(); j++) {
                    if (Constants.usersIngredients.get(j)
                            .getName().toLowerCase().equals(ingredientName.toLowerCase())) {
                        ingredientExists = true;
                    }
                }

                if (validInput && !ingredientName.isEmpty() && !ingredientExists) {
                    i.setName(ingredientName);
                    i.setOptionalTag(optionalTag);
                    i.setPrimaryTag(primaryTag);
                    Constants.usersIngredients.add(i);
                    Collections.sort(Constants.usersIngredients, new SortBasedOnTag());
                    listViewAdapter.notifyDataSetChanged();
                    String json = CreateJSON.createIngredientsJSON(context, Constants.usersIngredients, true);
                    fileHelper.saveFile(json, context, Constants.INGREDIENTS_FILE_NAME);
                    dialog.dismiss();
                }
                else
                {
                    if (ingredientExists) {
                        String toastText = "Ingredient is already in your pantry";
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String toastText = "Invalid ingredient name. Please check input and try again.";
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void getIngredientData(Context context)
    {
        if (Constants.doesIngredientsFileExist) {
            try {
                String ingredientJSON = fileHelper.readFile(context, Constants.INGREDIENTS_FILE_NAME);
                Constants.usersIngredients = ParseJSON.parseIngredients(ingredientJSON);

                if (Constants.usersIngredients != null)
                {
                    Collections.sort(Constants.usersIngredients, new SortBasedOnTag());
                }
                else
                {
                    Constants.usersIngredients = new ArrayList<>();
                }
            } catch (JSONException e) {
                Log.e(TAG, "getIngredientData: ", e);
                Constants.usersIngredients = new ArrayList<>();
            }
        }
    }
}