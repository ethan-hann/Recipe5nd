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

package com.uhcl.recipe5nd.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingItemsAdapter;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;

public class ShoppingItemsFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShoppingItemsFragment: ";
    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private ShoppingItemsAdapter shoppingItemsAdapter;
    private Context context;
    private FileHelper fileHelper = new FileHelper();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Global.currentlyViewedShoppingList.getTitle());
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        context = getContext();

        addButton = rootView.findViewById(R.id.shoppingAddButton);
        addButton.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);

        shoppingItemsAdapter = new ShoppingItemsAdapter();
        recyclerView.setAdapter(shoppingItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return rootView;
    }

    //Swipe left to remove items from shopping list
    private ItemTouchHelper.SimpleCallback swipe(){
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT )
        {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int position = viewHolder.getAdapterPosition();
                Global.currentlyViewedShoppingList.getItems().remove(position);
                Global.currentlyViewedShoppingList.getIsCheckedArray().delete(position);
                String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                shoppingItemsAdapter.notifyItemRemoved(position);
            }
        };
    }


    @Override
    public void onClick(View view)
    {
        View dialogView =  LayoutInflater.from(getContext()).inflate(R.layout.shopping_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        AlertDialog alert = dialogBuilder.create();

        EditText dialogEditText = dialogView.findViewById(R.id.shoppingDialogEditText);
        dialogEditText.setVisibility(View.VISIBLE);
        Helper.showKeyboard(dialogEditText);

        TextView dialogTextView = dialogView.findViewById(R.id.shoppingDialogTextView);
        dialogTextView.setText(R.string.shopping_item_add);

        MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
        MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(dialogEditText.getText())) {
                    Toast.makeText(context, "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                }else {
                    Global.currentlyViewedShoppingList.addItem(dialogEditText.getText().toString());
                    String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                    fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                }
                shoppingItemsAdapter.notifyDataSetChanged();

                alert.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }
}
