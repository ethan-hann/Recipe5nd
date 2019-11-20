package com.uhcl.recipe5nd.helperClasses;

import java.util.Date;

public class ShoppingData
{
    private String title;
    private Date date;
    private String items;
    private String isChecked;

    public ShoppingData(String title, Date date, String items, String isChecked)
    {
        this.title = title;
        this.date = date;
        this.items = items;
        this.isChecked = isChecked;
    }

    public ShoppingData(String title, Date date)
    {
        this.title = title;
        this.date = date;
    }

    public String getTitle()
    {
        return title;
    }

    public Date getDate()
    {
        return date;
    }

    public String getItems()
    {
        return items;
    }

    public String isChecked()
    {
        return isChecked;
public class ShoppingData {
    private String name;
    private String date;

    public ShoppingData(String name, String date){
        this.name = name;
        this.date = date;
    }

    public String getDate(){
        return this.date;
    }

    public String getName(){
        return this.name;
    }

}
