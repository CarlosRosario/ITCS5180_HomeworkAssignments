package com.example.group26.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Carlos on 3/17/2016.
 */
public class NoteDAO {

    private SQLiteDatabase db;

    public NoteDAO(SQLiteDatabase db){
        this.db = db;
    }

    public long save(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_DATE, note.getDate());
        values.put(NoteTable.COLUMN_NOTE, note.getNote());
        return db.insert(NoteTable.TABLENAME, null, values);
    }

    public boolean update(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteTable.COLUMN_DATE, note.getDate());
        values.put(NoteTable.COLUMN_NOTE, note.getNote());
        return db.update(NoteTable.TABLENAME, values, NoteTable.COLUMN_KEY + "=?", new String[]{note.getCitykey()+""}) > 0;
    }

    public boolean delete(Note note){
        return db.delete(NoteTable.TABLENAME, NoteTable.COLUMN_KEY + "=?", new String[]{note.getCitykey() + ""}) > 0;
    }

    public Note getNoteById(long id){

        Note note = null;
        Cursor c  = db.query(true, NoteTable.TABLENAME, new String[]{NoteTable.COLUMN_KEY, NoteTable.COLUMN_DATE, NoteTable.COLUMN_NOTE}, NoteTable.COLUMN_KEY + "=?", new String[]{id + ""}, null, null, null, null, null);

        if(c != null && c.moveToFirst()){
            note = buildNoteFromCursor(c);

            if(!c.isClosed()){
                c.close();
            }
        }

        return note;
    }

    public List<Note> getAllNotes(){
        List<Note> notes = new ArrayList<Note>();
        Cursor c = db.query(true, NoteTable.TABLENAME, new String[]{NoteTable.COLUMN_KEY, NoteTable.COLUMN_DATE, NoteTable.COLUMN_NOTE}, null, null, null, null, null,null);

        if(c != null && c.moveToFirst()){

            do {
                Note note = buildNoteFromCursor(c);
                if(note != null){
                    notes.add(note);
                }

            }while(c.moveToNext());

            if(!c.isClosed()){
                c.close();
            }
        }

        return notes;
    }

    private Note buildNoteFromCursor(Cursor c){
        Note note = null;
        if(c != null){
            note = new Note();
            note.setCitykey(c.getLong(0));
            note.setDate(c.getString(1));
            note.setNote(c.getString(2));
        }
        return note;
    }
}
