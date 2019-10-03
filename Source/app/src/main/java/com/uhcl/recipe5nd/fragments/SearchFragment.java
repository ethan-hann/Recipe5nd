package com.uhcl.recipe5nd.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.backgroundTasks.FetchData;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchFragment extends Fragment
{
    public static ArrayList<Recipe> recipes = new ArrayList<>();
    private EditText searchParams;
    private TextView data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        Button button = view.findViewById(R.id.button);
        data = view.findViewById(R.id.fetched_data);
        searchParams = view.findViewById(R.id.searchTerm);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //make sure the text box is non-empty
                if (!TextUtils.isEmpty(searchParams.getText()))
                {
                    Helper.hideKeyboard(getActivity());
                    searchParams.clearFocus();
                    FetchData process = new FetchData();
                    try {
                        //Build URL from user input
                        URL searchURL = new URL(Constants.baseURL + Constants.API_KEY +
                                Constants.searchSuffix + searchParams.getText());

                        process.execute(searchURL);
                        recipes = process.get();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!recipes.isEmpty()) {
                        data.setText(recipes.get(0).getRecipeInformation()); //TODO: display all recipes found
                    }
                    else
                    {
                        data.setText("Sorry, no recipes could be found for " + searchParams.getText() + " :(");
                    }
                }
            }
        });
        return view;
    }
}
