package com.n.travelmap.Library.PlaceAPI.AutoCompleteAPI;

import com.n.travelmap.Library.PlaceAPI.PlacesException;

import java.util.List;

/**
 * Created by Khanh An on 12/20/17.
 */

public interface SearchPlacesListener {
    void onPlacesFailure(PlacesException e);

    void onPlacesStart();

    void onPlacesSuccess(List<SearchPlaceObject> places);

    void onPlacesFinished();
}