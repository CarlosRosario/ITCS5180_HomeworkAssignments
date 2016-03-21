package com.example.group26.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 3/18/2016.
 */
public class CityDAO {

    private SQLiteDatabase db;

    public CityDAO(SQLiteDatabase db){
        this.db = db;
    }

    public long saveCity(City city){
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITY, city.getCityName());
        values.put(CityTable.COLUMN_STATE, city.getState());
        values.put(CityTable.COLUMN_TEMPERATURE, city.getTemperature());
        return db.insert(CityTable.TABLENAME, null, values);
    }

    public boolean updateCity(City city){
        ContentValues values = new ContentValues();
        values.put(CityTable.COLUMN_CITY, city.getCityName());
        values.put(CityTable.COLUMN_STATE, city.getState());
        values.put(CityTable.COLUMN_TEMPERATURE, city.getTemperature());
        return db.update(CityTable.TABLENAME, values, CityTable.COLUMN_KEY + "=?", new String[]{city.getCitykey()+""}) > 0;
    }

    public boolean deleteCity(City city){
        return db.delete(CityTable.TABLENAME, CityTable.COLUMN_KEY + "=?", new String[]{city.getCitykey() + ""}) > 0;
    }

    public void deleteAllCities(){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM " + CityTable.TABLENAME);

        try {
            db.execSQL(sb.toString());
        } catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public City getCityById(long id){

        City city = null;
        Cursor c  = db.query(true, CityTable.TABLENAME, new String[]{CityTable.COLUMN_KEY, CityTable.COLUMN_CITY, CityTable.COLUMN_STATE, CityTable.COLUMN_TEMPERATURE}, CityTable.COLUMN_KEY + "=?", new String[]{id + ""}, null, null, null, null, null);

        if(c != null && c.moveToFirst()){
            city = buildCityFromCursor(c);

            if(!c.isClosed()){
                c.close();
            }
        }

        return city;
    }

    public List<City> getAllCities(){
        List<City> cities = new ArrayList<City>();
        Cursor c = db.query(true, CityTable.TABLENAME, new String[]{CityTable.COLUMN_KEY, CityTable.COLUMN_CITY, CityTable.COLUMN_STATE, CityTable.COLUMN_TEMPERATURE}, null, null, null, null, null,null);

        if(c != null && c.moveToFirst()){
            do {
                City city = buildCityFromCursor(c);
                if(city != null){
                    cities.add(city);
                }
            }while(c.moveToNext());

            if(!c.isClosed()){
                c.close();
            }
        }
        return cities;
    }

    private City buildCityFromCursor(Cursor c){

        City city = null;
        if(c != null){
            city = new City();
            city.setCitykey(c.getLong(0));
            city.setCityName(c.getString(1));
            city.setState(c.getString(2));
            city.setTemperature(c.getString(3));
        }

        return city;
    }

}
