package com.uhcl.recipe5nd.helperClasses;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ShoppingFile{

    private File file;
    private InputStream inputStream;
    private Context c;
    private JSONArray array;


    public ShoppingFile(Context c){
        this.c = c;
        file = new File(c.getFilesDir().getAbsolutePath() + "/" + "shopping.txt");
        try {
            if(file.length()==0){
                array = new JSONArray();
            }else{
                array = new JSONArray(readFile());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String readFile(){
        String ret = "";

        try {
            inputStream = c.openFileInput("shopping.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

        return ret;

    }


    public void writeFile(String name, String date, List<String> items){


        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONArray itemz = new JSONArray(items);

            JSONObject data = new JSONObject();

            data.put("Name",name);
            data.put("Date",date);
            data.put("Items",itemz);
            array.put(data);

            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONArray getJsonArray(){
        return this.array;
    }

    public List<ShoppingData> getData(){
        JSONObject temp;
        List<ShoppingData> data = new ArrayList<>();


        for(int i = 0; i<array.length();i++){
            try {
               temp = (JSONObject) array.get(i);
               data.add(new ShoppingData(temp.get("Name").toString(),temp.get("Date").toString(),getItems((JSONArray) temp.get("Items"))));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    private List<String> getItems(JSONArray x){
        List<String> temp = new ArrayList<>();
        for(int i = 0; i<x.length();i++){
            try {
                temp.add(x.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }


    public void removeItem(int i){
        array.remove(i);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void editName(int i, String s){

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            temp.put("Name",s);
            array.put(i,temp);
            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addItems(int i, String s){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray items = (JSONArray) temp.get("Items");
            items.put(s);
            temp.put("Items",items);
            array.put(i,temp);
            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void removeListItem(int i, int item){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray items = (JSONArray) temp.get("Items");
            items.remove(item);
            temp.put("Items",items);
            array.put(i,temp);
            writer.write(array.toString());

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<String> getItems(int i){

        try {
            JSONObject temp = (JSONObject) array.get(i);
            JSONArray items = (JSONArray) temp.get("Items");
            return getItems(items);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public void crossed(int i,int crossedPos, String crossed){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray crozzed = new JSONArray();

            if(temp.isNull("Crossed")){

                temp.put("Crossed",crozzed);
            }else{
                crozzed = (JSONArray) temp.get("Crossed");
            }

            crozzed.put(crossedPos,crossed);

            temp.put("Crossed",crozzed);
            array.put(i,temp);

            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getCrossed(int i, int crossedPos){
        try {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray crozzed = (JSONArray) temp.get("Crossed");
            if(crozzed.isNull(0)){
                return "";
            }else{


                String s = crozzed.get(crossedPos).toString();

                return s;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "false";
    }

    public void deleteCrossed(int i, int crossedPos){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray crozzed = new JSONArray();

            if(temp.isNull("Crossed")){

                temp.put("Crossed",crozzed);
            }else{
                crozzed = (JSONArray) temp.get("Crossed");
            }

            crozzed.remove(crossedPos);

            temp.put("Crossed",crozzed);
            array.put(i,temp);

            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void editItem(int i, int pos, String item){
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getPath()), "utf-8"))) {

            JSONObject temp = (JSONObject) array.get(i);
            JSONArray items = new JSONArray();

            if(temp.isNull("Items")){

                temp.put("Items",items);
            }else{
                items = (JSONArray) temp.get("Items");
            }

            items.put(pos,item);

            temp.put("Items",items);
            array.put(i,temp);

            writer.write(array.toString());


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile(){
        file.delete();
    }

}
