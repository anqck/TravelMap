package com.n.travelmap.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Khanh An on 12/19/17.
 */

public class DatabaseProvider {


    private SQLiteDatabase myDatabase;

    DatabaseProvider(Context context)
    {

        myDatabase = context.openOrCreateDatabase("MyApp1", MODE_PRIVATE, null);

        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS SEARCHHISTORY(SearchID INTEGER  PRIMARY KEY   AUTOINCREMENT, PlaceName TEXT,Address TEXT, PlaceID TEXT, Latitude DOUBLE, Longitude DOUBLE,Types TEXT,Img BLOB);");

   /* Create a Table in the Database. */

    }

    public void ExecuseNonQuery(String str)
    {
        myDatabase.execSQL(str);
    }


    public SQLiteDatabase GetSQLiteDatabase()
    {
        return  this.myDatabase;
    }

    public Cursor ExecuseQuery(String str)
    {
        return myDatabase.rawQuery(str , null);
    }
}
