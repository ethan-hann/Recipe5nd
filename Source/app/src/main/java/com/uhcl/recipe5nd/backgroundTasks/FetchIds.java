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

package com.uhcl.recipe5nd.backgroundTasks;

import android.os.AsyncTask;

import com.uhcl.recipe5nd.helperClasses.APIConnector;
import com.uhcl.recipe5nd.helperClasses.ParseJSON;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

/**
 * Fetches recipe IDs from the API on a separate thread other than the UI thread.
 */
public class FetchIds extends AsyncTask<URL, Void, ArrayList<String>>
{
    @Override
    protected ArrayList<String> doInBackground(URL... urls)
    {
        ArrayList<String> ids = new ArrayList<>();

        try {
            boolean canConnect = APIConnector.executeQuery(urls[0]);
            if (canConnect) {
                ids = ParseJSON.parseIDS(APIConnector.apiResponse);
                return ids;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return ids;
    }
}