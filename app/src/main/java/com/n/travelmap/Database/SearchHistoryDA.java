package com.n.travelmap.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 12/19/17.
 */

public class SearchHistoryDA {



    DatabaseProvider databaseProvider;

    public SearchHistoryDA(Context context)
    {
        databaseProvider = new DatabaseProvider(context);

    }

    public void AddSearchHistory(SavedPlace place)
    {

        String sql                      =   "INSERT INTO SEARCHHISTORY(PlaceName,Address, PlaceID,Latitude,Longitude, Types, Img) VALUES(?,?,?,?,?,?,?)";

        SQLiteStatement insertStmt      =    databaseProvider.GetSQLiteDatabase().compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,place.getName());
        insertStmt.bindString(2,place.getAddress());
        insertStmt.bindString(3, place.getPlaceID());
        insertStmt.bindDouble(4, place.getLocation().latitude);
        insertStmt.bindDouble(5,place.getLocation().longitude);
        insertStmt.bindString(6, place.getTypes());
        insertStmt.bindBlob(7, place.getBytesImg());
        insertStmt.executeInsert();



        //databaseProvider.ExecuseNonQuery("INSERT INTO SEARCHHISTORY(PlaceName,Address, PlaceID,Latitude,Longitude, Types) VALUES ('"+place.getName()+"','"+place.getAddress() +"','"+place.getPlaceID()+ "'," + place.getLocation().latitude+"," + place.getLocation().longitude + ",'" +place.getTypes() +"');");
    }

    public List<SavedPlace> GetSearchHistory()
    {
        List<SavedPlace> res = new ArrayList<>();

        Cursor c = databaseProvider.ExecuseQuery("SELECT * FROM SEARCHHISTORY");

        c.moveToFirst();
        if (c != null)
        {
            if(c.getCount() != 0)
            {
                // Loop through all Results
                do {
                    String PlaceName = c.getString(c.getColumnIndex("PlaceName"));
                    String PlaceID = c.getString(c.getColumnIndex("PlaceID"));
                    String Address = c.getString(c.getColumnIndex("Address"));
                    LatLng latLng = new LatLng( c.getDouble(c.getColumnIndex("Latitude")), c.getDouble(c.getColumnIndex("Longitude")));

                    String Types = c.getString(c.getColumnIndex("Types"));
                    byte[] blob = c.getBlob(c.getColumnIndex("Img"));

                    res.add(new SavedPlace(PlaceName,PlaceID,latLng,Address,Types,blob));

                }
                while(c.moveToNext());
            }

        }

        return res;
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

}
