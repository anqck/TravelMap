package com.n.travelmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MarkerTagObject extends Object implements Serializable
{
        private String placeID;
        private double latitude, longitude;
        private String tag;
        //private transient LatLng  latLng;

    MarkerTagObject()
    {
        placeID = "";
        //latLng = null;
    }

    MarkerTagObject(String placeID)
    {
        this.placeID = placeID;
        tag = "";
        //latLng = null;
    }
    public MarkerTagObject(String placeID, LatLng location)
    {
        this.placeID = placeID;
        latitude = location.latitude;
        longitude = location.longitude;
        tag = "";
        //latLng = location;
    }

    public MarkerTagObject(String placeID, LatLng location, String tag)
    {
        this.placeID = placeID;
        latitude = location.latitude;
        longitude = location.longitude;
        this.tag = tag;
        //latLng = location;
    }

    public LatLng getLatLng() {
        return new LatLng(latitude,longitude);
    }

    public void setLatLng(LatLng location) {
        latitude = location.latitude;
        longitude = location.longitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
