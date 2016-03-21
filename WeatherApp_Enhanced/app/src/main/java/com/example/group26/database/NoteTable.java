package com.example.group26.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Carlos on 3/17/2016.
 */
public class NoteTable {

    static final String TABLENAME = "notes";
    static final String COLUMN_KEY = "citykey";
    static final String COLUMN_DATE = "date";
    static final String COLUMN_NOTE = "note";

    static public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_KEY + " integer primary key autoincrement, ");
        sb.append(COLUMN_DATE + " text, ");
        sb.append(COLUMN_NOTE + " text);");

        try {
            String query = sb.toString();
            Log.d("query1", query);
            db.execSQL(query);
        } catch (SQLException ex){
            Log.d("query1", ex.toString());
            ex.printStackTrace();
        }
    }

    static public void onUpdate(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        NoteTable.onCreate(db);
    }

}
