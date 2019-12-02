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

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper {
    private static final String TAG = "FileHelper";

    /**
     * Clears data from all files in the supplied context's files directory
     * @param context : the application's context
     * @return true if data deletion was successful; false if not
     */
    public boolean clearAllData(Context context)
    {
        try {
            createBlankFile(context, Global.INGREDIENTS_FILE_NAME);
            createBlankFile(context, Global.SHOPPING_LIST_FILE_NAME);
            createBlankFile(context, Global.FAVORITES_FILE_NAME);
            return true;
        } catch (NullPointerException e) {
            Log.e(TAG, "deleteAllFiles: ", e);
            return false;
        }
    }

    /**
     * Saves a file to the private file directory of the application
     * @param s        : the string to save
     * @param context  : the context of the application
     * @param fileName : the fileName the file should be saved as
     * @return boolean : true if saving was successful, false if not
     */
    public boolean saveFile(String s, Context context, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath().concat("/" + fileName)), false);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(s);
            osw.flush();
            osw.close();
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Saving file failed: ", e);
        }
        return false;
    }


    /**
     * Reads a saved file into a string
     * @param context  : the context of the application
     * @param fileName : the fileName to read from
     * @return String : contents of the file or an exception if the file is not found
     */
    public String readFile(Context context, String fileName) {
        try {
            if (exists(context, fileName))
            {
                FileInputStream fis = new FileInputStream(new File(context.getFilesDir().getAbsolutePath().concat("/" + fileName)));
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    builder.append(line);
                }
                fis.close();
                isr.close();
                br.close();
                return builder.toString();
            }
            else
            {
                throw new FileNotFoundException();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: ", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not read file: ", e);
        }
        return "";
    }

    /**
     * Creates a file with a single JSON array like so: []
     * @param context : the application's context
     * @param fileName : the file to create
     * @return true if file is created successfully; false if not
     */
    private boolean createBlankFile(Context context, String fileName)
    {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath().concat("/"+fileName)), false);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write("[]");
            osw.flush();
            osw.close();
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "createBlankFile: ", e);
        }
        return false;
    }

    /**
     * Checks in the supplied context for the file
     * @param context : the application's context
     * @param fileName : the file to search for
     * @return true if file exists; false if not
     */
    public boolean exists(Context context, String fileName)
    {
        String[] files = context.fileList();
        for (String file : files)
        {
            if (file.equals(fileName))
            {
                switch (fileName) {
                    case Global.INGREDIENTS_FILE_NAME:
                    {
                        Global.doesIngredientsFileExist = true;
                        break;
                    }
                    case Global.FAVORITES_FILE_NAME:
                    {
                        Global.doesFavoritesExist = true;
                        break;
                    }
                    case Global.SHOPPING_LIST_FILE_NAME:
                    {
                        Global.doesShoppingListExist = true;
                        break;
                    }
                    default:
                    {
                        Log.i(TAG, "exists: file exists but is not of known type");
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a file if it doesn't exist. This method calls createBlankFile() if a file does not
     * exist
     * @param context : the application's context
     * @param fileName : the file to search for
     */
    void createIfNotExists(Context context, String fileName)
    {
        if (!exists(context, fileName))
        {
            createBlankFile(context, fileName);
            if (createBlankFile(context, fileName)) {
                switch (fileName) {
                    case Global.INGREDIENTS_FILE_NAME: {
                        Global.doesIngredientsFileExist = true;
                        break;
                    }
                    case Global.FAVORITES_FILE_NAME: {
                        Global.doesFavoritesExist = true;
                        break;
                    }
                    case Global.SHOPPING_LIST_FILE_NAME: {
                        Global.doesShoppingListExist = true;
                        break;
                    }
                    default: {
                        Log.i(TAG, "createIfNotExists: file exists but is not of known type");
                    }
                }
            }
        }
    }
}
