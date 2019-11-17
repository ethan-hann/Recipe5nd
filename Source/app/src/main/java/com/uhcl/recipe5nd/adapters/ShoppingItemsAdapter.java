package com.uhcl.recipe5nd.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.helperClasses.ShoppingFile;

import java.util.List;

public class ShoppingItemsAdapter extends RecyclerView.Adapter<ShoppingItemsAdapter.ShoppingItemViewHolder>{
    private List<String> items;
    private int x;
    private ShoppingFile file;

    public ShoppingItemsAdapter(List<String> items, int x, ShoppingFile file){
        this.items = items;
        this.x = x;
        this.file = file;
    }


    @NonNull
    @Override
    public  ShoppingItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shopping_list_row_items, viewGroup, false);
        return new ShoppingItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ShoppingItemViewHolder viewHolder, int i) {

        viewHolder.item.setText(items.get(i));
        viewHolder.item.setPaintFlags(0);

        if(file.getCrossed(x,i).equals("true")){
            viewHolder.item.setPaintFlags(viewHolder.item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        viewHolder.item.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if(file.getCrossed(x,i).equals("false")){
                    file.crossed(x,i,"true");
                    viewHolder.item.setPaintFlags(viewHolder.item.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    notifyDataSetChanged();

                }else{
                    file.crossed(x,i, "false");
                    viewHolder.item.setPaintFlags(0);
                    notifyDataSetChanged();

                }
            }
        });

        viewHolder.item.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View view) {
                return true;
            }

        });

    }



    @Override
    public int getItemCount(){return items.size();}

    public void setItems(List<String> items) {
        this.items = items;
    }


    public class ShoppingItemViewHolder extends RecyclerView.ViewHolder{
        private TextView item;
        private LinearLayout parent;


        public ShoppingItemViewHolder(View itemView){
            super(itemView);
            parent = itemView.findViewById(R.id.shoppingListParent);
            item = itemView.findViewById(R.id.shoppingItem);

        }


    }

}
