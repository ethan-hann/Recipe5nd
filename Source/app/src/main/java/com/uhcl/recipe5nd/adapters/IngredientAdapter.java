package com.uhcl.recipe5nd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>
{
    private static final String TAG = "IngredientAdapter: ";
    private Context context;
    private FileHelper fileHelper = new FileHelper();

    @Override
    public int getItemCount() {
        return Constants.usersIngredients == null ? 0 : Constants.usersIngredients.size();
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position)
    {
        try {
            holder.bind(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        context = view.getContext();

        return new IngredientAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView ingredientNameText;
        TextView ingredientOptionalTagText;
        TextView ingredientPrimaryTagText;
        RelativeLayout parent;

        ViewHolder(View view)
        {
            super(view);
            ingredientNameText = view.findViewById(R.id.ingredient_name);
            ingredientOptionalTagText = view.findViewById(R.id.ingredient_optional_tag);
            ingredientPrimaryTagText = view.findViewById(R.id.ingredient_primary_tag);
            parent = view.findViewById(R.id.ingredient_item_parent);
        }

        void bind(int pos)
        {
            ingredientNameText.setText(Constants.usersIngredients.get(pos).getName());
            ingredientOptionalTagText.setText(Constants.usersIngredients.get(pos).getOptionalTag());
            ingredientPrimaryTagText.setText(Constants.usersIngredients.get(pos).getPrimaryTag().name());

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.ingredient_dialog, null);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    dialogBuilder.setCancelable(true);
                    dialogBuilder.setView(dialogView);
                    AlertDialog dialog = dialogBuilder.create();

                    TextView textView = dialogView.findViewById(R.id.ingredient_dialog_text_view);
                    textView.setText(R.string.edit_ingredient);

                    EditText ingNameTextBox = dialogView.findViewById(R.id.ingredient_dialog_edit_name);
                    ingNameTextBox.setText(Constants.usersIngredients.get(pos).getName());
                    Helper.showKeyboard(ingNameTextBox);

                    EditText ingOptionalTagBox = dialogView.findViewById(R.id.ingredient_dialog_edit_opt_tag);
                    ingOptionalTagBox.setText(Constants.usersIngredients.get(pos).getOptionalTag());

                    Spinner primaryTagSpinner = dialogView.findViewById(R.id.ingredient_dialog_spinner);
                    ArrayAdapter<PrimaryTag> spinnerAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_item, PrimaryTag.values());
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    primaryTagSpinner.setAdapter(spinnerAdapter);

                    primaryTagSpinner.setSelection(spinnerAdapter.getPosition(Constants.usersIngredients.get(pos).getPrimaryTag()));

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

                            if (validInput && !ingredientName.isEmpty()) {
                                Constants.usersIngredients.remove(pos);
                                i.setName(ingredientName);
                                i.setOptionalTag(optionalTag);
                                i.setPrimaryTag(primaryTag);
                                Constants.usersIngredients.add(i);
                                notifyDataSetChanged();
                                String json = CreateJSON.createIngredientsJSON(context, Constants.usersIngredients, true);
                                fileHelper.saveFile(json, context, Constants.INGREDIENTS_FILE_NAME);
                                dialog.dismiss();
                            }
                            else
                            {
                                String toastText = "Invalid ingredient name. Please check input and try again.";
                                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
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
            });
        }
    }
}
