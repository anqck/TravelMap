package com.n.travelmap.Library.PlaceAPI.AutoCompleteAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.n.travelmap.Library.PlaceAPI.PlacesException;
import com.n.travelmap.Library.PlaceAPI.PlacesParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public class SearchPlaces extends AsyncTask<Void, Void, List<SearchPlaceObject>> {
    private PlacesException exception;

    private String Query;
    private String Key;

    protected final String PARAM_KEY = "key=";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private List<SearchPlacesListener> listeners;

    public SearchPlaces(Builder builder)
    {
        this.listeners = new ArrayList<>();
        registerListener(builder.listener);

        this.Key = builder.key;
        this.Query = builder.query;
    }

    protected SearchPlaces(SearchPlacesListener listener) {

        this.listeners = new ArrayList<>();
        registerListener(listener);
    }

    @Override
    protected List<SearchPlaceObject> doInBackground(Void... params) {
        try {


            List<SearchPlaceObject> pair =  new PlacesParser(constructURL()).parseAutoComplete();
            dispatchOnSuccess(pair);



            return pair;
        } catch (PlacesException e) {
            exception = e;
            dispatchOnFailure(e);
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    protected void onPreExecute() {
        dispatchOnStart();
    }

    @Override
    protected void onPostExecute(List<SearchPlaceObject> places) {
        if (places == null) {
            dispatchOnFailure(exception);
        } else {
            dispatchOnFinished();
        }

    }

    protected String constructURL() throws UnsupportedEncodingException {

        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        sb.append("?key=" + this.Key);
        sb.append("&language=vi");
        sb.append("&location=10.870249,106.803735");
        // sb.append("&types=(cities)");
        sb.append("&input=" + URLEncoder.encode(Query, "utf8"));

        Log.e("Places", sb.toString());
        return sb.toString();
    }

    public void registerListener(SearchPlacesListener mListener) {
        if (mListener != null) {
            listeners.add(mListener);
        }
    }

    protected void dispatchOnStart() {
        for (SearchPlacesListener mListener : listeners) {
            mListener.onPlacesStart();
        }
    }

    protected void dispatchOnFailure(PlacesException exception) {
        Log.e("Places", exception.toString());
        for (SearchPlacesListener mListener : listeners) {
            mListener.onPlacesFailure(exception);

        }
    }

    protected void dispatchOnSuccess(List<SearchPlaceObject> places) {


        for (SearchPlacesListener mListener : listeners) {
            mListener.onPlacesSuccess(places);
        }
    }

    private void dispatchOnFinished() {
        for (SearchPlacesListener mListener : listeners) {
            mListener.onPlacesFinished();
        }
    }

    public static final class Builder {
        private String key;
        private String query;
        private SearchPlacesListener listener;

        public Builder() {
        }


        public SearchPlaces.Builder key(String val) {
            key = val;
            return this;
        }



        public SearchPlaces.Builder query(String val) {
            query = val;
            return this;
        }


        public SearchPlaces build() {
            return new SearchPlaces(this);
        }

        public SearchPlaces.Builder setListener(SearchPlacesListener listener) {
            this.listener = listener;

            return this;
        }


    }

}
