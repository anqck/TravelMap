package com.n.travelmap.Library.PlaceAPI;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


import com.n.travelmap.Activity.SearchActivity.SearchPlaceObject;
import com.n.travelmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 12/19/17.
 */

public class PlaceAPI {

    private static final String TAG = PlaceAPI.class.getSimpleName();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    //private static final String API_KEY = "AIzaSyA4kytOW-wj96y5zSueTfiIuswMWq3ZSrM";

    private Context context;
    public PlaceAPI(FragmentActivity activity) {
        context = activity;
    }

    public ArrayList<SearchPlaceObject> autocomplete (String input) {
        ArrayList<SearchPlaceObject> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + context.getString(R.string.google_maps_key));
            sb.append("&language=vi");
            sb.append("&location=10.870249,106.803735");
           // sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Log.d(TAG, jsonResults.toString());

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<SearchPlaceObject>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                List<String> types = new ArrayList<>();
                for(int j = 0 ; j < predsJsonArray.getJSONObject(i).getJSONArray("types").length(); j++)
                {
                    types.add(predsJsonArray.getJSONObject(i).getJSONArray("types").get(j).toString());
                }

                resultList.add(new SearchPlaceObject(predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("main_text"),predsJsonArray.getJSONObject(i).getJSONObject("structured_formatting").getString("secondary_text"), types,predsJsonArray.getJSONObject(i).getString("place_id")));




            }

        } catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }
}
