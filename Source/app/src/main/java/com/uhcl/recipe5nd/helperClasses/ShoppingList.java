/*
 *     Recipe5nd - Reverse recipe lookup application for Android
 *     Copyright (C) 2019 Manuel Berlanga
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

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingList
{
    private String title = "";
    private Date date;
    private ArrayList<String> items = new ArrayList<>();
    private SparseBooleanArray isCheckedArray = new SparseBooleanArray();

    public void addItem(String string)
    {
        items.add(string);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setChecked(int index, boolean value)
    {
        isCheckedArray.put(index, value);
    }

    public String getTitle()
    {
        return title;
    }

    public Date getDate()
    {
        return date;
    }

    public ArrayList<String> getItems()
    {
        return items;
    }

    public boolean isChecked(int index)
    {
        return isCheckedArray.get(index);
    }

    public SparseBooleanArray getIsCheckedArray()
    {
        return isCheckedArray;
    }
}
