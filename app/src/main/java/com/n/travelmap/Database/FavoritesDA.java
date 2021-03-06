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
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.n.travelmap.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Khanh An on 01/03/18.
 */

public class FavoritesDA {

    DatabaseProvider databaseProvider;
    Context context;

    public FavoritesDA(Context context)
    {
        databaseProvider = DatabaseProvider.GetInstance();
        this.context = context;
    }

    public void AddFavorites(FavoritesDTO place)
    {
        long oldSize = CountRecord();

        String sql                      =   "INSERT INTO FAVORITES(Lat,Long,PlaceID,Img,PlaceName,Address) VALUES(?,?,?,?,?,?)";

        SQLiteStatement insertStmt      =    DatabaseProvider.GetInstance().GetSQLiteDatabase().compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindString(1,String.valueOf(place.getLatLng().latitude));
        insertStmt.bindString(2,String.valueOf(place.getLatLng().longitude));
        insertStmt.bindString(3, place.getPlaceID());
        if(place.getBitmap() != null)
            insertStmt.bindBlob(4, place.getBytesImg());
        else
        {
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),   R.drawable.no_image);
            //Bitmap bitmap = drawableToBitmap(context.getResources().getDrawable(R.drawable.no_image));
            insertStmt.bindBlob(4, getBitmapAsByteArray(icon));
        }
        insertStmt.bindString(5,place.getTitle());
        insertStmt.bindString(6, place.getSubTitle());

        insertStmt.executeInsert();

        if(oldSize < CountRecord())
        {
            Toast.makeText(context,"Thêm thành công",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,"Thêm thất bại",Toast.LENGTH_SHORT).show();
        }


        //databaseProvider.ExecuseNonQuery("INSERT INTO SEARCHHISTORY(PlaceName,Address, PlaceID,Latitude,Longitude, Types) VALUES ('"+place.getName()+"','"+place.getAddress() +"','"+place.getPlaceID()+ "'," + place.getLocation().latitude+"," + place.getLocation().longitude + ",'" +place.getTypes() +"');");
    }

    public static List<FavoritesDTO> GetFavorites()
    {
        List<FavoritesDTO> res = new ArrayList<>();

        Cursor c = DatabaseProvider.GetInstance().ExecuseQuery("SELECT * FROM FAVORITES");

        c.moveToFirst();
        if (c != null)
        {
            if(c.getCount() != 0)
            {
                // Loop through all Results
                do {
                    Double lat = Double.parseDouble(c.getString(c.getColumnIndex("Lat")));
                    Double Long = Double.parseDouble(c.getString(c.getColumnIndex("Long")));
                    String PlaceID = c.getString(c.getColumnIndex("PlaceID"));

                    byte[] blob = c.getBlob(c.getColumnIndex("Img"));
                    String PlaceName = c.getString(c.getColumnIndex("PlaceName"));
                    String Address = c.getString(c.getColumnIndex("Address"));

                    res.add(new FavoritesDTO(new LatLng(lat,Long),PlaceID,blob,PlaceName,Address));

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

    public static boolean ifExists(FavoritesDTO model)
    {
        if(DatabaseProvider.GetInstance() == null)
            return false;

//        Cursor cursor = null;
//        String checkQuery = "SELECT * FROM FAVORITES WHERE Lat ='" +model.getLatLng().latitude+"' AND Long = '"+ model.getLatLng().longitude +"';";
//        cursor= DatabaseProvider.GetInstance().GetSQLiteDatabase().rawQuery(checkQuery,null);
//        boolean exists = (cursor.getCount() > 0);
//        cursor.close();
//        return exists;

        for(FavoritesDTO place :GetFavorites())
        {
            if(place.getLatLng().toString().compareTo(model.getLatLng().toString()) == 0)
            {
                return true;
            }

        }
        return false;
    }

    public void DeleteFavorites(FavoritesDTO model) {
        long oldSize = CountRecord();

        String checkQuery = "DELETE FROM FAVORITES WHERE Lat  ='" +model.getLatLng().latitude+"' AND Long = '"+ model.getLatLng().longitude +"'";
        DatabaseProvider.GetInstance().ExecuseNonQuery(checkQuery);

        if(oldSize > CountRecord())
        {
            Toast.makeText(context,"Xóa thành công",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context,"Xóa thất bại",Toast.LENGTH_SHORT).show();
        }
    }

    public static long CountRecord()
    {
        String sql = "SELECT COUNT(*) FROM FAVORITES";
        SQLiteStatement statement =   DatabaseProvider.GetInstance().GetSQLiteDatabase().compileStatement(sql);

        return statement.simpleQueryForLong();
    }
}
