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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingAdapter;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;
import com.uhcl.recipe5nd.helperClasses.ShoppingList;
import com.uhcl.recipe5nd.helperClasses.SortBasedOnDate;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ShoppingFragment extends Fragment implements View.OnClickListener
{
    private static final String TAG = "ShoppingFragment: ";
    private RecyclerView recyclerView;
    private ShoppingAdapter shoppingAdapter;
    private FloatingActionButton addButton;
    private Context context;
    private FileHelper fileHelper = new FileHelper();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Shopping Lists");
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        context = getContext();

        addButton =  rootView.findViewById(R.id.shoppingAddButton);
        addButton.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);

        getShoppingData(context);
        if (Global.shoppingLists != null) {
            shoppingAdapter = new ShoppingAdapter();
            shoppingAdapter.notifyDataSetChanged();

            recyclerView.setAdapter(shoppingAdapter);

            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return rootView;
    }

    /**
     * Get shopping data from a saved file
     * @param context : the application's context
     */
    private void getShoppingData(Context context)
    {
        if (Global.doesShoppingListExist) {
            try {
                String shoppingJSON = fileHelper.readFile(context, Global.SHOPPING_LIST_FILE_NAME);
                Global.shoppingLists = ParseJSON.parseShoppingLists(shoppingJSON);
                if (Global.shoppingLists != null) {
                    Collections.sort(Global.shoppingLists, new SortBasedOnDate());
                }
                else
                {
                    Global.shoppingLists = new ArrayList<>();
                }
            } catch (JSONException e) {
                Log.e(TAG, "getShoppingData: ", e);
            }
        }
    }

    @Override
    public void onClick(View view) {
        View dialogView =  LayoutInflater.from(context).inflate(R.layout.shopping_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setCancelable(true);
        dialogBuilder.setView(dialogView);
        AlertDialog alert = dialogBuilder.create();

        EditText text = dialogView.findViewById(R.id.shoppingDialogEditText);
        text.setVisibility(View.VISIBLE);
        Helper.showKeyboard(text);

        TextView textView = dialogView.findViewById(R.id.shoppingDialogTextView);
        textView.setText(R.string.shopping_dialog_title_input);

        MaterialButton okButton = dialogView.findViewById(R.id.shoppingDialogOk);
        MaterialButton cancelButton = dialogView.findViewById(R.id.shoppingDialogCancel);

        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (text.getText().toString().isEmpty())
                {
                    ShoppingList s = new ShoppingList();
                    s.setTitle("New Shopping List");
                    s.setDate(Calendar.getInstance().getTime());
                    Global.shoppingLists.add(s);
                    Collections.sort(Global.shoppingLists, new SortBasedOnDate());
                    String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                    fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                } else
                {
                    ShoppingList s = new ShoppingList();
                    s.setTitle(text.getText().toString());
                    s.setDate(Calendar.getInstance().getTime());
                    Global.shoppingLists.add(s);
                    Collections.sort(Global.shoppingLists, new SortBasedOnDate());
                    String json = CreateJSON.createShoppingListsJSON(context, Global.shoppingLists, true);
                    fileHelper.saveFile(json, context, Global.SHOPPING_LIST_FILE_NAME);
                }

                shoppingAdapter.notifyDataSetChanged();
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


