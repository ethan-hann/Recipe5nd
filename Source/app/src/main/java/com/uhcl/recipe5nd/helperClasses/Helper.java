package com.uhcl.recipe5nd.helperClasses;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    public static void showKeyboard(final EditText editText)
    {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                editText.dispatchTouchEvent(
                        MotionEvent.obtain(SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN , 0, 0, 0));
                editText.dispatchTouchEvent(
                        MotionEvent.obtain(SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_UP , 0, 0, 0));
            }
        }, 200);
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
        return !Constants.validator.matcher(str).find();
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
