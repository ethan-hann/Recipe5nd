package com.uhcl.recipe5nd.helperClasses;

import java.io.Serializable;
import java.util.List;

public class ShoppingData implements Serializable,Comparable<ShoppingData> {
    private String name;
    private String date;
    private List<String> items;
    private int datePos;

    public ShoppingData(String name, String date, List<String> items ){
        this.name = name;
        this.date = date;
        this.items = items;
    }


    public String getDate(){
        return this.date;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){this.name = name;}

    public void setDate(String date){this.date = date;}

    public void setItems(List<String> items){
        this.items = items;
    }

    public List<String> getItems(){
        return this.items;
    }

    public void setDatePos(int datePos){
        this.datePos = datePos;
    }

    public int getDatePos(){
        return this.datePos;
    }

    @Override
    public int compareTo(ShoppingData shoppingData) {
        return this.getDatePos() - shoppingData.getDatePos();
    }
}
