package com.uhcl.recipe5nd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.adapters.RecipeAdapter;
import com.uhcl.recipe5nd.helperClasses.Constants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultsFragment extends Fragment
{
    private RecyclerView recyclerView;
    private RecipeAdapter recyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        recyclerView = rootView.findViewById(R.id.search_results_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new RecipeAdapter(Constants.returnedRecipesFromSearch);

        recyclerView.setAdapter(recyclerAdapter);

        recyclerAdapter.notifyDataSetChanged();

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return rootView;
    }
}
