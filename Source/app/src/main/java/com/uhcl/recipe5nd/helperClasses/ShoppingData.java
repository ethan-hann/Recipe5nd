package com.uhcl.recipe5nd.helperClasses;

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

    public void setName(String name){this.name = name;}

    public void setDate(String date){this.date = date;}

}
