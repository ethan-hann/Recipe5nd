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
        return !Global.validator.matcher(str).find();
    }
}
