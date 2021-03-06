package com.n.travelmap.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.n.travelmap.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 12/19/17.
 */

public class SearchHistoryDA {



    DatabaseProvider databaseProvider;
    Context context;

    public SearchHistoryDA(Context context)
    {
        databaseProvider = DatabaseProvider.GetInstance();
        this.context = context;
    }

    public void AddSearchHistory(SearchHistoryDTO place)
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

        if(place.getBitmap() != null)
            insertStmt.bindBlob(7, place.getBytesImg());
        else
        {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),   R.drawable.no_image);
            //Bitmap bitmap = drawableToBitmap(context.getResources().getDrawable(R.drawable.no_image));
            insertStmt.bindBlob(7, getBitmapAsByteArray(icon));
        }

        insertStmt.executeInsert();



        //databaseProvider.ExecuseNonQuery("INSERT INTO SEARCHHISTORY(PlaceName,Address, PlaceID,Latitude,Longitude, Types) VALUES ('"+place.getName()+"','"+place.getAddress() +"','"+place.getPlaceID()+ "'," + place.getLocation().latitude+"," + place.getLocation().longitude + ",'" +place.getTypes() +"');");
    }

    public List<SearchHistoryDTO> GetSearchHistory()
    {
        List<SearchHistoryDTO> res = new ArrayList<>();

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

                    res.add(new SearchHistoryDTO(PlaceName,PlaceID,latLng,Address,Types,blob));

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

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);

        Log.v("Bitmap width - Height :", width + " : " + height);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}
