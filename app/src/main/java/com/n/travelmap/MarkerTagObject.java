package com.n.travelmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MarkerTagObject extends Object implements Serializable
{
        private String placeID;
        private transient LatLng  latLng;

    MarkerTagObject()
    {
        placeID = "";
        latLng = null;
    }

    MarkerTagObject(String placeID)
    {
        this.placeID = placeID;
        latLng = null;
    }
    public MarkerTagObject(String placeID, LatLng location)
    {
        this.placeID = placeID;
        latLng = location;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}
