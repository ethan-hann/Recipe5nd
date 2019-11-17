package com.uhcl.recipe5nd.adapters;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.ShoppingItemsFragment;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.ShoppingData;
import com.uhcl.recipe5nd.helperClasses.ShoppingFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {

    private final ShoppingFile file;
    private List<ShoppingData> shoppingList;
    private ShoppingItemsFragment itemsFrag = new ShoppingItemsFragment();
    private Bundle bundle = new Bundle();
    private List<String> items = new ArrayList();

    public ShoppingAdapter(List<ShoppingData> shoppingList, ShoppingFile file){
        this.shoppingList = shoppingList;
        this.file = file;
    }

    public void setShoppingList(List<ShoppingData> list){
        this.shoppingList =list;
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



        viewHolder.parent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                bundle.putSerializable("items",data);
                bundle.putInt("i",i);
                itemsFrag.setArguments(bundle);

                AppCompatActivity activity = Helper.unwrap(v.getContext());
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack("k")
                        .replace(R.id.fragment_container, itemsFrag)
                        .commit();



            }
        });

        viewHolder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                View dailogView =  LayoutInflater.from(v.getContext()).inflate(R.layout.shopping_dialog, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setCancelable(true);

                builder1.setView(dailogView);
                AlertDialog alert = builder1.create();


                EditText text = dailogView.findViewById(R.id.shoppingDialogEditText);
                text.setVisibility(View.GONE);

                TextView textView = dailogView.findViewById(R.id.shoppingDialogTextView);
                textView.setText("Delete this shopping list?");

                Button ok = dailogView.findViewById(R.id.shoppingDialogOk);
                Button cancel = dailogView.findViewById(R.id.shoppingDialogCancel);

                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        shoppingList.remove(i);
                        file.removeItem(i);
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
        }

        );


        viewHolder.shoppingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dailogView =  LayoutInflater.from(v.getContext()).inflate(R.layout.shopping_dialog, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setCancelable(true);

                builder1.setView(dailogView);
                AlertDialog alert = builder1.create();


                EditText text = dailogView.findViewById(R.id.shoppingDialogEditText);
                text.setText(data.getName());
                text.setVisibility(View.VISIBLE);

                TextView textView = dailogView.findViewById(R.id.shoppingDialogTextView);
                textView.setText("Edit Shopping List Title");

                Button ok = dailogView.findViewById(R.id.shoppingDialogOk);
                Button cancel = dailogView.findViewById(R.id.shoppingDialogCancel);

                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        data.setName(text.getText().toString());
                        file.editName(i,text.getText().toString());
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


            }
        });


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
