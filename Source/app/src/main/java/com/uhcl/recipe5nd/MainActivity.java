package com.uhcl.recipe5nd;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.uhcl.recipe5nd.backgroundTasks.FetchData;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.Recipe;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

//TODO: create string references in strings.xml
//TODO: make 'catch' branches more specific
public class MainActivity extends AppCompatActivity {

    public static ArrayList<Recipe> recipes = new ArrayList<>();
    private EditText searchParams;
    private TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        data = findViewById(R.id.fetched_data);
        searchParams = findViewById(R.id.search_param);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


    }
}
