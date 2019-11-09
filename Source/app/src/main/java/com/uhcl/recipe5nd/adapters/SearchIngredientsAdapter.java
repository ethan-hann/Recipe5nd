package com.uhcl.recipe5nd.adapters;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom adapter class for populating the recycler view with items
 * Includes a nested class @ViewHolder that defines the individual item within the recycler view
 */

public class SearchIngredientsAdapter extends RecyclerView.Adapter<SearchIngredientsAdapter.ViewHolder>
{
    private static final String TAG = "SearchIngAdapter: ";
    private ArrayList<Ingredient> usersIngredients;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public SearchIngredientsAdapter(ArrayList<Ingredient> ingredients) {
        this.usersIngredients = ingredients;
        Constants.selectedIngredients = new ArrayList<>();
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
        if (usersIngredients == null)
        {
            return 0;
        }
        return usersIngredients.size();
    }

    public void updateList(ArrayList<Ingredient> newList) {
        usersIngredients = newList;
        notifyDataSetChanged();
    }

    public void clearSelectedItems()
    {
        Constants.selectedIngredients = new ArrayList<>();
        itemStateArray = new SparseBooleanArray();
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
            if (!itemStateArray.get(adapterPos, false)) { //check the item
                ingredientItem.setChecked(true);
                itemStateArray.put(adapterPos, true);
                Constants.selectedIngredients.add(usersIngredients.get(adapterPos));
                Log.i(TAG, "Ingredient added to search: ".concat(usersIngredients.get(adapterPos).getName()));
            } else { //uncheck the item
                ingredientItem.setChecked(false);
                itemStateArray.put(adapterPos, false);
                Constants.selectedIngredients.remove(usersIngredients.get(adapterPos));
                Log.i(TAG, "Ingredient removed from search: ".concat(usersIngredients.get(adapterPos).getName()));
            }
        }
    }
}
