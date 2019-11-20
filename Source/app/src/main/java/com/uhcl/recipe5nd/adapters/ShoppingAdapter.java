package com.uhcl.recipe5nd.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.*;
import androidx.recyclerview.widget.RecyclerView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.ShoppingData;
import java.util.List;
import java.util.Random;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {

    private List<ShoppingData> shoppingList;

    public ShoppingAdapter(List<ShoppingData> shoppingList){
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public  ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_list_row, viewGroup, false);
        return new ShoppingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder viewHolder, int i){
        ShoppingData data = shoppingList.get(i);
        Random k = new Random();
        int color = Color.argb(255,k.nextInt(255),k.nextInt(255),k.nextInt(255));
        viewHolder.parent.setBackgroundColor(color);
        viewHolder.shoppingName.setText(data.getName());
        viewHolder.shoppingDate.setText(data.getDate());
    }

    @Override
    public int getItemCount(){return shoppingList.size();}

    public class ShoppingViewHolder extends RecyclerView.ViewHolder{
        private TextView shoppingName, shoppingDate;
        private LinearLayout parent;

        public ShoppingViewHolder(View itemView){
            super(itemView);
            parent = itemView.findViewById(R.id.shoppingListParent);
            shoppingName = itemView.findViewById(R.id.shoppingName);
            shoppingDate = itemView.findViewById(R.id.shoppingDate);
        }
    }

}
