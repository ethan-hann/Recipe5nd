package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.ShoppingAdapter;
import com.uhcl.recipe5nd.helperClasses.ShoppingData;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {

    private RecyclerView recyclerView;
    private ShoppingAdapter shoppingAdapter;
    private List<ShoppingData> shoppingList = new ArrayList<>();

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){

        shoppingList.add(new ShoppingData("k","12/12/12"));
        shoppingList.add(new ShoppingData("k","12/12/12"));
        shoppingList.add(new ShoppingData("k","12/12/12"));
        shoppingList.add(new ShoppingData("k","12/12/12"));
        shoppingList.add(new ShoppingData("k","12/12/12"));
        shoppingList.add(new ShoppingData("k","12/12/12"));

        View rootView = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        recyclerView = rootView.findViewById(R.id.shoppingList_recycler_view);
        shoppingAdapter = new ShoppingAdapter(shoppingList);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(shoppingAdapter);

        return rootView;
    }

}
