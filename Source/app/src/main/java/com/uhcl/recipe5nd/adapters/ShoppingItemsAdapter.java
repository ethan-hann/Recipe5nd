package com.uhcl.recipe5nd.adapters;

import android.app.AlertDialog;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
            public boolean onLongClick(View v) {

                View dailogView =  LayoutInflater.from(v.getContext()).inflate(R.layout.shopping_dialog, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setCancelable(true);

                builder1.setView(dailogView);
                AlertDialog alert = builder1.create();


                EditText text = dailogView.findViewById(R.id.shoppingDialogEditText);
                text.setText(file.getItems(x).get(i));
                text.setSelection(file.getItems(x).get(i).length());
                text.setVisibility(View.VISIBLE);

                TextView textView = dailogView.findViewById(R.id.shoppingDialogTextView);
                textView.setText("Edit Shopping List Item");

                Button ok = dailogView.findViewById(R.id.shoppingDialogOk);
                Button cancel = dailogView.findViewById(R.id.shoppingDialogCancel);

                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        file.editItem(x,i,text.getText().toString());
                        items.set(i,file.getItems(x).get(i));
                        notifyDataSetChanged();
                        alert.dismiss();
                    }

                });

                cancel.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {

                        alert.dismiss();

                    }

                });

                alert.show();


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
