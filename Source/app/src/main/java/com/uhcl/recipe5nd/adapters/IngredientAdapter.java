package com.uhcl.recipe5nd.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.Ingredient;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IngredientAdapter extends ArrayAdapter<Ingredient>
{
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients)
    {
        super(context, 0, ingredients);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Ingredient i = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_ingredient, parent, false);
        }

        TextView ingredientName = convertView.findViewById(R.id.ingredient_name);
        TextView ingredientPrimaryTag = convertView.findViewById(R.id.ingredient_primary_tag);
        TextView ingredientOptionalTag = convertView.findViewById(R.id.ingredient_optional_tag);

        ingredientName.setText("Name: ".concat(i.getName()));
        ingredientPrimaryTag.setText("Prim. Tag: ".concat(i.getPrimaryTag().name()));
        ingredientOptionalTag.setText("Opt. Tag: ".concat(i.getOptionalTag()));

        return convertView;
    }
}
