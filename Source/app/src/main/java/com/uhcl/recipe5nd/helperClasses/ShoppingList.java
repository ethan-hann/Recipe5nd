package com.uhcl.recipe5nd.helperClasses;

import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingList
{
    private String title;
    private Date date;
    private ArrayList<String> items = new ArrayList<>();
    private SparseBooleanArray isCheckedArray = new SparseBooleanArray();

    public ShoppingList(String title, Date date, ArrayList<String> items, SparseBooleanArray isChecked)
    {
        this.title = title;
        this.date = date;
        this.items = items;
        this.isCheckedArray = isChecked;
    }

    public ShoppingList(String title, Date date)
    {
        this.title = title;
        this.date = date;
    }

    public ShoppingList()
    {

    }

    public void addItem(String string)
    {
        items.add(string);
    }

    public void removeItem(String string)
    {
        items.remove(string);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setChecked(int index) {
        if (isCheckedArray.get(index))
        {
            isCheckedArray.put(index, false);
        }
        else
        {
            isCheckedArray.put(index, true);
        }
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
