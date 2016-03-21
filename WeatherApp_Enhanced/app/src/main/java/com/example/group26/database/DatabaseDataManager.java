package com.example.group26.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Carlos on 3/18/2016.
 */
public class DatabaseDataManager {

    private Context mContext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private NoteDAO noteDAO;
    private CityDAO cityDAO;

    public DatabaseDataManager(Context context){
        this.mContext = context;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        noteDAO = new NoteDAO(db);
        cityDAO = new CityDAO(db);
    }

    public void close(){
        if(db != null){
            db.close();
        }
    }

    public long saveNote(Note note){
        return this.noteDAO.save(note);
    }

    public boolean updateNote(Note note){
        return this.noteDAO.update(note);
    }

    public boolean deleteNote(Note note){
        return this.noteDAO.delete(note);
    }

    public Note getNoteById(long id){
        return this.noteDAO.getNoteById(id);
    }

    public List<Note> getAllNotes(){
        return this.noteDAO.getAllNotes();
    }

    public long saveCity(City city){
        return this.cityDAO.saveCity(city);
    }

    public boolean updateCity(City city){
        return this.cityDAO.updateCity(city);
    }

    public boolean deleteCity(City city){
        return this.cityDAO.deleteCity(city);
    }

    public void deleteAllCities() { this.cityDAO.deleteAllCities();}

    public City getCityById(long id){
        return this.cityDAO.getCityById(id);
    }

    public List<City> getAllCities(){
        return this.cityDAO.getAllCities();
    }

}
