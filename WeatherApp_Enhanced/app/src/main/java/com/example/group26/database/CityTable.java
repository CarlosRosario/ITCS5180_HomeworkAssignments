package com.example.group26.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Carlos on 3/17/2016.
 */
public class CityTable {

    static final String TABLENAME = "cities";
    static final String COLUMN_KEY = "citykey";
    static final String COLUMN_CITY = "cityname";
    static final String COLUMN_STATE = "state";
    static final String COLUMN_TEMPERATURE = "temperature";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_KEY + " integer primary key autoincrement, ");
        sb.append(COLUMN_CITY + " text not null, ");
        sb.append(COLUMN_STATE + " text not null, ");
        sb.append(COLUMN_TEMPERATURE + " text not null);");

        try {
            String query = sb.toString();
            Log.d("query2", query);
            db.execSQL(query);
        } catch (SQLException ex){
            Log.d("query2", ex.toString());
            ex.printStackTrace();
        }
    }

    static public void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        CityTable.onCreate(db);
    }
}
