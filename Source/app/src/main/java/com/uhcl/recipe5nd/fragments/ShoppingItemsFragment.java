package com.uhcl.recipe5nd.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingItemsAdapter;
import com.uhcl.recipe5nd.helperClasses.ShoppingData;
import com.uhcl.recipe5nd.helperClasses.ShoppingFile;

import java.util.List;

public class ShoppingItemsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingItemsAdapter shoppingItemsAdapter;
    private List<String> items;
    private Bundle bundle;
    private ShoppingData data;
    private ShoppingFile file;
    private int i;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        file = new ShoppingFile(getContext());

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        FloatingActionButton add =  rootView.findViewById(R.id.shoppingAddButton);

        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);

        bundle = getArguments();
        data = (ShoppingData) bundle.getSerializable("items");
        i = bundle.getInt("i");
        items = data.getItems();


        shoppingItemsAdapter = new ShoppingItemsAdapter(items,i,file);

        addButton(add);
        recyclerView.setAdapter(shoppingItemsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipe());
        itemTouchHelper.attachToRecyclerView(recyclerView);


        return rootView;
    }



    private void addButton(FloatingActionButton add){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View dailogView =  LayoutInflater.from(getContext()).inflate(R.layout.shopping_dialog, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setCancelable(true);

                builder1.setView(dailogView);
                AlertDialog alert = builder1.create();


                Button ok = dailogView.findViewById(R.id.shoppingDialogOk);
                Button cancel = dailogView.findViewById(R.id.shoppingDialogCancel);
                EditText text = dailogView.findViewById(R.id.shoppingDialogEditText);
                text.setVisibility(View.VISIBLE);

                TextView textView = dailogView.findViewById(R.id.shoppingDialogTextView);
                textView.setText("Enter Shopping List Items");

                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (text.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getContext(), "Item cannot be empty.", Toast.LENGTH_SHORT);

                            toast.show();
                        }else{
                            file.addItems(i,text.getText().toString());
                        }

                        shoppingItemsAdapter.setItems(file.getItems(i));
                        shoppingItemsAdapter.notifyDataSetChanged();

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

    private ItemTouchHelper.SimpleCallback swipe(){
        return new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                int position = viewHolder.getAdapterPosition();

                file.removeListItem(i,position);
                file.deleteCrossed(i,position);
                shoppingItemsAdapter.setItems(file.getItems(i));

                shoppingItemsAdapter.notifyDataSetChanged();

            }
        };
    }



}
