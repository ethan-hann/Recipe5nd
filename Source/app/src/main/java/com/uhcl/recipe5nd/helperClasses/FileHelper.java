package com.uhcl.recipe5nd.helperClasses;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileHelper
{
    private static final String TAG = "FileHelper";

    /**
     * Saves a file to the private file directory of the application
     * @param s : the string to save
     * @param context : the context of the application
     * @param fileName : the fileName the file should be saved as
     * @return boolean : true if saving was successful, false if not
     */
    public boolean saveFile(String s, Context context, String fileName)
    {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(s);
            outputStreamWriter.close();
            Log.i(TAG, "saveFile: " + context.getFilesDir() + "/" + fileName);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Saving file failed: ", e);
        }

        return false;
    }

    /**
     * Reads into a string a saved file.
     * @param context : the context of the application
     * @param fileName : the fileName to read from
     * @return String : contents of the file,
     * or an empty string if the file could not be read or found
     */
    public String readFile(Context context, String fileName)
    {
        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                StringBuilder builder = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }

                inputStream.close();
                return builder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: ", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not read file: ", e);
        }
        return "";
    }
}
