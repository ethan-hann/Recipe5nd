package com.uhcl.recipe5nd.helperClasses;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIConnector
{
    private static final String TAG = "APIConnector";
    public static String apiResponse;
    private static HttpURLConnection httpURLConnection;
    private static InputStream inputStream;
    private static BufferedReader bufferedReader;
    private static StringBuilder builder;

    /**
     * Queries the supplied url and generates an HTTP response
     * @param url the url to executeQuery
     * @return boolean true if the executeQuery was successful; false if not
     */
    public static boolean executeQuery(URL url)
    {
        try
        {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            inputStream = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            builder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }

            apiResponse = builder.toString();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Could not connect to API: ", e);
            return false;
        }
    }

    /**
     * Builds a query string to be sent to the Fetch classes based on a QueryType and string params
     * @param type the QueryType this executeQuery will be
     * @param param string parameters; customize the query
     * @return a String representing the executeQuery url or in the default case, null
     */
    public static String buildQueryString(QueryType type, String param) {
        String base;
        switch (type) {
            case SEARCH_BY_NAME:
                builder = new StringBuilder();
                base = Constants.BASE_URL.concat(Constants.NAME_SUFFIX);
                builder.append(base);

                builder.append(param);

                return builder.toString();
            case SEARCH_BY_ID:
                builder = new StringBuilder();
                base = Constants.BASE_URL.concat(Constants.ID_SUFFIX);
                builder.append(base);

                builder.append(param);

                return builder.toString();
            case SEARCH_BY_INGREDIENTS:
                builder = new StringBuilder();
                base = Constants.BASE_URL.concat(Constants.INGREDIENT_SEARCH_SUFFIX);
                builder.append(base);

                for (int i = 0; i < Constants.selectedIngredients.size() - 1; i++) {
                    builder.append(Constants.selectedIngredients.get(i).getName());
                    builder.append(",");
                }
                builder.append(Constants.selectedIngredients.get(Constants.selectedIngredients.size() - 1).getName());

                return builder.toString();
            default:
                Log.d(TAG, "buildQuery() called with: type = [" + type + "], params = [" + param + "]");
                return null;
        }
    }
}
