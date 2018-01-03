package com.n.travelmap.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;

/**
 * Created by Khanh An on 01/03/18.
 */

public class FavoritesDTO {

    private  LatLng latLng;
    private  String placeID;
    private Bitmap bitmap;
    private  String Title;
    private String subTitle;
    private  String Types;

    FavoritesDTO()
    {
        latLng = null;
        placeID = "";
    }

    public FavoritesDTO(LatLng latLng, String placeID)
    {
        this.latLng = latLng;
        this.placeID = placeID;
    }

    public FavoritesDTO(LatLng latLng, String placeID, byte[] bytesImg, String title, String subTitle)
    {
        this.latLng = latLng;
        this.placeID = placeID;
        this.bitmap = getImage(bytesImg);
        this.Title = title;
        this.subTitle = subTitle;
    }

    public FavoritesDTO(LatLng latLng, String placeID, Bitmap bitmap, String title, String subTitle)
    {
        this.latLng = latLng;
        this.placeID = placeID;
        this.bitmap = bitmap;
        this.Title = title;
        this.subTitle = subTitle;
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
