package com.uhcl.recipe5nd.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.ShoppingItemsFragment;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.ShoppingList;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnDate;

import java.util.Collections;
import java.util.Locale;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>
{
    private static final String TAG = "ShoppingAdapter: ";
    private Context context;
    private FileHelper fileHelper = new FileHelper();

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_list_row, parent, false);
        context = parent.getContext();

        return new ShoppingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position)
    {
        try {
            holder.bind(position);
        } catch (IndexOutOfBoundsException e) {
            Log.e(TAG, "onBindViewHolder: ", e);
        }
    }

    @Override
    public int getItemCount(){return Constants.shoppingLists.size();}

    class ShoppingViewHolder extends RecyclerView.ViewHolder
    {
        MaterialCardView parent;
        TextView shoppingName;
        TextView shoppingDate;

        ShoppingViewHolder(View itemView)
        {
            super(itemView);
            parent = itemView.findViewById(R.id.shoppingListParent2);
            shoppingName = itemView.findViewById(R.id.shoppingTitle);
            shoppingDate = itemView.findViewById(R.id.shoppingDate);
        }

        void bind(int position)
        {
            ShoppingList s = Constants.shoppingLists.get(position);
            shoppingName.setText(String.format(Locale.US, "%s\n%d items", s.getTitle(), s.getItems().size()));
            shoppingName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            shoppingDate.setText(s.getDate().toString());

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constants.currentlyViewedShoppingList = Constants.shoppingLists.get(position);

                    AppCompatActivity activity = Helper.unwrap(view.getContext());
                    activity.getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack("shopping_list")
                            .replace(R.id.fragment_container, new ShoppingItemsFragment())
                            .commit();
                }
            });

            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.shopping_dialog, null);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                    dialogBuilder.setCancelable(true);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alert = dialogBuilder.create();

                    EditText dialogEditText = dialogView.findViewById(R.id.shoppingDialogEditText);
                    dialogEditText.setVisibility(View.GONE);

                    TextView dialogTextView = dialogView.findViewById(R.id.shoppingDialogTextView);
                    dialogTextView.setText(R.string.shopping_dialog_delete_confirm);

                    MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
                    MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Constants.shoppingLists.remove(position);
                            String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
                            fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
                            Collections.sort(Constants.shoppingLists, new SortBasedOnDate());
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

            shoppingName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.shopping_dialog, null);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
                    dialogBuilder.setCancelable(true);
                    dialogBuilder.setView(dialogView);
                    AlertDialog alert = dialogBuilder.create();

                    EditText dialogEditText = dialogView.findViewById(R.id.shoppingDialogEditText);
                    dialogEditText.setText(s.getTitle());
                    dialogEditText.setSelection(s.getTitle().length());
                    dialogEditText.setVisibility(View.VISIBLE);
                    Helper.showKeyboard(dialogEditText);

                    TextView dialogTextView = dialogView.findViewById(R.id.shoppingDialogTextView);
                    dialogTextView.setText(R.string.shopping_list_title_edit);

                    MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
                    MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

                    okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            s.setTitle(dialogEditText.getText().toString());
                            String json = CreateJSON.createShoppingListsJSON(context, Constants.shoppingLists, true);
                            fileHelper.saveFile(json, context, Constants.SHOPPING_LIST_FILE_NAME);
                            Collections.sort(Constants.shoppingLists, new SortBasedOnDate());
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
                }
            });
        }
    }
}
