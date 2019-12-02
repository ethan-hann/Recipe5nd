/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Ethan D. Hann
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.uhcl.recipe5nd.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.uhcl.recipe5nd.R;
import com.uhcl.recipe5nd.fragments.EditIngredientsFragment;
import com.uhcl.recipe5nd.fragments.SearchFragment;
import com.uhcl.recipe5nd.fragments.ShoppingFragment;
import com.uhcl.recipe5nd.fragments.FavoriteRecipesFragment;
import com.uhcl.recipe5nd.helperClasses.Global;
import com.uhcl.recipe5nd.helperClasses.FileHelper;
import com.uhcl.recipe5nd.helperClasses.Helper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawer;
    private NavigationView navView;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

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
        Global.init(this);

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

        navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new EditIngredientsFragment()).commit();
            navView.setCheckedItem(R.id.nav_pantry);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_search:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SearchFragment()).commit();
                break;
            }
            case R.id.nav_pantry:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new EditIngredientsFragment()).commit();
                break;
            }
            case R.id.nav_shopping:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShoppingFragment()).commit();
                break;
            }
            case R.id.nav_favorites:
            {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoriteRecipesFragment()).commit();
                break;
            }
            case R.id.nav_deleteData:
            {
                View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_confirm_dialog, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setCancelable(true);
                dialogBuilder.setView(dialogView);
                AlertDialog dialog = dialogBuilder.create();

                TextView alertText = dialogView.findViewById(R.id.delete_confirm_text_view);
                alertText.setText(R.string.delete_confirmation);

                MaterialButton okButton = dialogView.findViewById(R.id.delete_dialog_ok);
                MaterialButton cancelButton = dialogView.findViewById(R.id.delete_dialog_cancel);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FileHelper fileHelper = new FileHelper();
                        fileHelper.clearAllData(context);
                        Global.usersIngredients = new ArrayList<>();
                        Global.shoppingLists = new ArrayList<>();
                        Global.favoriteRecipes = new ArrayList<>();
                        dialog.dismiss();
                        navView.setCheckedItem(R.id.nav_pantry);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new EditIngredientsFragment()).commit();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            }
            case R.id.nav_about:
            {
                View dialogView = LayoutInflater.from(this).inflate(R.layout.about_dialog, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setCancelable(true);
                dialogBuilder.setView(dialogView);
                AlertDialog dialog = dialogBuilder.create();

                TextView alertText = dialogView.findViewById(R.id.about_dialog_text_view);
                alertText.setText(R.string.about_text);

                MaterialButton okButton = dialogView.findViewById(R.id.about_dialog_ok);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            }
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
