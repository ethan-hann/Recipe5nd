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
     * Saves a file to the private file directory of the application
     *
     * @param s        : the string to save
     * @param context  : the context of the application
     * @param fileName : the fileName the file should be saved as
     * @return boolean : true if saving was successful, false if not
     */
    public boolean saveFile(String s, Context context, String fileName) {
        try {
            //Checking if file exists before saving. This determines if we need to write or append
            if (!exists(context, fileName))
            {
                FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath().concat("/"+fileName)), false);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
                outputStreamWriter.write(s);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                Log.i(TAG, "saveFile written: " + context.getFilesDir() + "/" + fileName);
            } else
            {
                FileOutputStream fos = new FileOutputStream(new File(context.getFilesDir().getAbsolutePath().concat("/"+fileName)), true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
                outputStreamWriter.append(s);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                Log.i(TAG, "saveFile appended: " + context.getFilesDir() + "/" + fileName);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Saving file failed: ", e);
        }
        return false;
    }

    /**
     * Reads a saved file into a string.
     *
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
                isr.close();
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
     * Checks in the supplied context for the file. Also sets global vars to match
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
                return true;
            }
        }
        return false;
    }
}
