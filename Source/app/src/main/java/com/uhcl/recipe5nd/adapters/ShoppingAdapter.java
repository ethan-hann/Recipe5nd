/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Manuel Berlanga
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.ShoppingList;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnDate;

import org.json.JSONException;

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
    public int getItemCount() {
        return Global.shoppingLists == null ? 0 : Global.shoppingLists.size();
    }

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
            FileHelper f = new FileHelper();
            String json = f.readFile(context, Global.SHOPPING_LIST_FILE_NAME);
            try {
                Global.shoppingLists = ParseJSON.parseShoppingLists(json);
            } catch (JSONException e)
            {
                Log.e(TAG, "ShoppingViewHolder: ", e);
            }

        }

        void bind(int position)
        {
            ShoppingList s = Global.shoppingLists.get(position);
            if (s.getItems().size() == 1)
            {
                shoppingName.setText(String.format(Locale.US, "%s\n%d item", s.getTitle(), s.getItems().size()));
            }
            else
            {
                shoppingName.setText(String.format(Locale.US, "%s\n%d items", s.getTitle(), s.getItems().size()));
            }

            shoppingName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            shoppingDate.setText(s.getDate().toString());

            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Global.currentlyViewedShoppingList = Global.shoppingLists.get(position);

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
                            Global.shoppingLists.remove(position);
                            String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                            fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                            Collections.sort(Global.shoppingLists, new SortBasedOnDate());
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
                            String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                            fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                            Collections.sort(Global.shoppingLists, new SortBasedOnDate());
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
