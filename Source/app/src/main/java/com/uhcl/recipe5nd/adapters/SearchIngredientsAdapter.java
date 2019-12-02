/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Ethan D. Hann
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

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.Ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchIngredientsAdapter extends RecyclerView.Adapter<SearchIngredientsAdapter.ViewHolder>
{
    private static final String TAG = "SearchIngAdapter: ";
    private ArrayList<Ingredient> usersIngredients;
    private SparseBooleanArray itemStateArray;

    public SearchIngredientsAdapter(ArrayList<Ingredient> ingredients) {
        this.usersIngredients = ingredients;
        Global.selectedIngredients = new ArrayList<>();

        itemStateArray = new SparseBooleanArray();
        initItemStates();
    }

    /**
     * Initializes SparseBooleanArray with false values
     */
    private void initItemStates()
    {
        for (int i = 0; i < usersIngredients.size(); i++)
        {
            itemStateArray.put(i, false);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item_search, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return usersIngredients == null ? 0 : usersIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private CheckedTextView ingredientItem;

        ViewHolder(View view)
        {
            super(view);
            ingredientItem = view.findViewById(R.id.ingredient_item_checkbox);
            view.setOnClickListener(this);
        }

        void bind(int pos) {
            if (!itemStateArray.get(pos, false))
            {
                ingredientItem.setChecked(false);
            } else {
                ingredientItem.setChecked(true);
            }
            ingredientItem.setText(String.valueOf(usersIngredients.get(pos).getName()));
        }

        @Override
        public void onClick(View view)
        {
            int adapterPos = getAdapterPosition();
            if (!itemStateArray.get(adapterPos, false))
            { //check the item
                ingredientItem.setChecked(true);
                itemStateArray.put(adapterPos, true);
                Global.selectedIngredients.add(usersIngredients.get(adapterPos));
            } else { //uncheck the item
                ingredientItem.setChecked(false);
                itemStateArray.put(adapterPos, false);
                Global.selectedIngredients.remove(usersIngredients.get(adapterPos));
            }
        }
    }
}
