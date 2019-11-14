package com.uhcl.recipe5nd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.navigation.NavigationView;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.EditIngredientsFragment;
import com.uhcl.recipe5nd.fragments.SearchFragment;
import com.uhcl.recipe5nd.helperClasses.Constants;
import com.uhcl.recipe5nd.helperClasses.CreateJSON;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;
import com.uhcl.recipe5nd.helperClasses.Ingredient;
import com.uhcl.recipe5nd.helperClasses.PrimaryTag;

import java.util.ArrayList;

//TODO: create string references in strings.xml
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Using a Toolbar instead of an Action bar to adhere to Material Design
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Changing color of status bar if our ANDROID BUILD is above SDK 21
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primaryLightColor));
        }

        //Initialize files
        Constants.init(this);

        /*
         * TODO: TEMPORARY CODE BELOW THIS LINE: REMOVE BEFORE PRODUCTION
         */
        FileHelper fileHelper = new FileHelper();
        ArrayList<Ingredient> testIngredients = new ArrayList<>();
        testIngredients.add(new Ingredient("Chicken", PrimaryTag.HOT));
        testIngredients.add(new Ingredient("Beef", PrimaryTag.COLD, "Meats"));
        String json = CreateJSON.createIngredientsJSON(this, testIngredients);
        fileHelper.saveFile(json, this, Constants.INGREDIENTS_FILE_NAME);
        /*
         * TODO: TEMPORARY CODE ABOVE THIS LINE: REMOVE BEFORE PRODUCTION
         */

        drawer = findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes.
                // The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
                Helper.hideKeyboard(drawer, MainActivity.this);
            }
        });

        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new SearchFragment()).commit();
            navView.setCheckedItem(R.id.nav_search);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) { //TODO: add everyone's fragments
            case R.id.nav_search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            case R.id.nav_pantry:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditIngredientsFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

}
