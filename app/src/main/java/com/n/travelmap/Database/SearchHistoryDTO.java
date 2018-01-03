package com.n.travelmap.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;
import com.n.travelmap.Activity.SearchActivity.SearchPlaceObject;

import java.io.ByteArrayOutputStream;


/**
 * Created by Khanh An on 12/19/17.
 */

public class SearchHistoryDTO {
    private String Name;
    private String Address;
    private String PlaceID;

    private LatLng Location;
    private  String Types;

    private Bitmap bitmap;

    SearchHistoryDTO()
    {

    }

    public SearchHistoryDTO(SearchPlaceObject obj, LatLng location, Bitmap bitmap)
    {
        this.Name = obj.getName();
        this.Address = obj.getSubTitle();
        this.PlaceID = obj.getPlaceID();

        this.Location = location;

        this.Types = obj.GetTypesTrans().toString();

        this.bitmap = bitmap;
    }

    SearchHistoryDTO(String name, String placeid)
    {
        this.Name = name;
        this.PlaceID = placeid;
        this.Location = new LatLng(0,0);
    }

    SearchHistoryDTO(String name, String placeid, LatLng location)
    {
        this.Name = name;
        this.PlaceID = placeid;
        this.Location = location;
    }


    SearchHistoryDTO(String name, String placeid, LatLng location, String address, String types, byte[]  bytesImg)
    {
        this.Name = name;
        this.PlaceID = placeid;
        this.Location = location;
        this.Address = address;
        this.Types = types;
        this.bitmap = getImage(bytesImg);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public LatLng getLocation() {
        return Location;
    }

    public void setLocation(LatLng location) {
        Location = location;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }


    public String getTypes() {
        return Types;
    }

    public void setTypes(String types) {
        Types = types;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public byte[] getBytesImg() {
        return getBytes(bitmap) ;
    }


    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
