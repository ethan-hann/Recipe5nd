package com.uhcl.recipe5nd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;

//TODO: change this class to use a global Constants.currentlyViewedShoppingList
//      to match the other classes.

public class ShoppingItemsAdapter extends RecyclerView.Adapter<ShoppingItemsAdapter.ShoppingItemViewHolder>
{
    private static final String TAG = "ShoppingItemsAdapter: ";
    private Context context;
    private FileHelper fileHelper = new FileHelper();

    @NonNull
    @Override
    public ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_row_items, parent, false);

        context = parent.getContext();

        return new ShoppingItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ShoppingItemViewHolder holder, int position)
    {
        try {
            holder.bind(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount(){return Constants.currentlyViewedShoppingList.getItems().size();}

    class ShoppingItemViewHolder extends RecyclerView.ViewHolder
    {
        TextView itemTextView;
        LinearLayout parent;

        ShoppingItemViewHolder(View itemView)
        {
            super(itemView);
            parent = itemView.findViewById(R.id.shoppingListParent);
            itemTextView = itemView.findViewById(R.id.shopping_list_item);
        }

        void bind(int position)
        {
            itemTextView.setText(Constants.currentlyViewedShoppingList.getItems().get(position));
            itemTextView.setPaintFlags(0);

            if (Constants.currentlyViewedShoppingList.isChecked(position)) {
                itemTextView.setPaintFlags(itemTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            itemTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Constants.currentlyViewedShoppingList.isChecked(position))
                    {
                        Constants.currentlyViewedShoppingList.setChecked(position, true);
                        itemTextView.setPaintFlags(itemTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
                        fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
                        notifyDataSetChanged();
                    }
                    else
                    {
                        Constants.currentlyViewedShoppingList.setChecked(position, false);
                        itemTextView.setPaintFlags(0);
                        String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
                        fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
                        notifyDataSetChanged();
                    }
                }
            });

            itemTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.shopping_dialog, null);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                    dialogBuilder.setCancelable(true);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alert = dialogBuilder.create();

                    EditText dialogEditText = dialogView.findViewById(R.id.shoppingDialogEditText);
                    dialogEditText.setText(Constants.currentlyViewedShoppingList.getItems().get(position));
                    dialogEditText.setSelection(Constants.currentlyViewedShoppingList.getItems().get(position).length());
                    dialogEditText.setVisibility(View.VISIBLE);
                    Helper.showKeyboard(dialogEditText);

                    TextView dialogTextView = dialogView.findViewById(R.id.shoppingDialogTextView);
                    dialogTextView.setText(R.string.shopping_dialog_item_edit);

                    MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
                    MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Constants.currentlyViewedShoppingList.getItems().remove(position);
                            Constants.currentlyViewedShoppingList.getItems().add(position, dialogEditText.getText().toString());
                            String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
                            fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
                            notifyDataSetChanged();
                            alert.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });

                    alert.show();
                    return true;
                }
            });
        }
    }

}
