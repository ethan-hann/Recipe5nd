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
}
