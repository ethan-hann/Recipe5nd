package com.uhcl.recipe5nd.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingAdapter;
import com.uhcl.recipe5nd.helperClasses.ShoppingData;
import com.uhcl.recipe5nd.helperClasses.ShoppingFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingAdapter shoppingAdapter;
    private List<ShoppingData> shoppingList;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
    private ShoppingFile file;


    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){


        file = new ShoppingFile(getContext());

        shoppingList = file.getData();


        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        FloatingActionButton add =  rootView.findViewById(R.id.shoppingAddButton);
        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);

        shoppingAdapter = new ShoppingAdapter(shoppingList, file);


        addButton(add);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(shoppingAdapter);


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
                textView.setText("Enter Shopping List Title");

                ok.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (text.getText().toString().isEmpty()) {
                            file.writeFile("New Shopping List ",dateFormatter.format(new Date()),new ArrayList<>() );
                        }else{
                            file.writeFile(text.getText().toString(),dateFormatter.format(new Date()),new ArrayList<>() );
                        }

                        shoppingAdapter.setShoppingList(file.getData());
                        shoppingAdapter.notifyDataSetChanged();
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


}


