package com.uhcl.recipe5nd.helperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Helper
{
    private static final String TAG = "Helper";
    public static void hideKeyboard(View pView, Activity pActivity) {
        if (pView == null) {
            pView = pActivity.getWindow().getCurrentFocus();
        }
        if (pView != null) {
            InputMethodManager imm = (InputMethodManager) pActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(pView.getWindowToken(), 0);
            }
        }
    }

    public static void hideKeyboard(Activity activity) {
        try{
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View currentFocusedView = activity.getCurrentFocus();
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static AppCompatActivity unwrap(Context context)
    {
        while (!(context instanceof AppCompatActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (AppCompatActivity) context;
    }

    public static boolean validateInput(String str)
    {
        //checking for illegal chars
        for (int i = 0; i < Constants.invalidCharacters.length; i++) {
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == Constants.invalidCharacters[i])
                {
                    Log.d(TAG, "validateInput: " + str.charAt(j) + "\t" + Constants.invalidCharacters[i]);
                    return false;
                }
            }
        }

        //checking for numbers
        for (int i = 0; i < str.length(); i++)
        {
            try {
                int a = Integer.parseInt(String.format("%c", str.charAt(i)));
            } catch (NumberFormatException e)
            {
                return false;
            }
        }
        return true;
    }

    public static ArrayList<Recipe> removeDuplicateRecipes(ArrayList<Recipe> list){
        LinkedHashMap<String, Recipe> tempMap = new LinkedHashMap<String, Recipe>();
        ArrayList<Recipe> uniqueList = new ArrayList<Recipe>();
        try {
            Recipe oldKey;

            for(int i=0; i<list.size(); i++){
                oldKey = tempMap.put(list.get(i).getId(), list.get(i));
                if(oldKey != null) System.out.println("Recipe: "+oldKey.getId()+" : "+oldKey.getStrMeal()+" is already in the list!");
            }

            Iterator itr = tempMap.entrySet().iterator();
            while(itr.hasNext()){
                Map.Entry entry = (Map.Entry)itr.next();
                Recipe recipe = (Recipe)entry.getValue();
                uniqueList.add(recipe);
            }

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Error removing duplicates");
        }
        return uniqueList;
    }
}
