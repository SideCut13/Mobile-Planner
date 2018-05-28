package com.example.ania.mobileplanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, DBContract.DATABASE_NAME, null, DBContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DBContract.DBEntry.TABLE_NAME + " (" +
                DBContract.DBEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DBContract.DBEntry.COLUMN_NAME_TITLE + " TEXT," +
                DBContract.DBEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                DBContract.DBEntry.COLUMN_NAME_DATE + " TEXT," +
                DBContract.DBEntry.COLUMN_NAME_TIME + " TEXT," +
                DBContract.DBEntry.COLUMN_NAME_NOTIFICATION + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.DBEntry.TABLE_NAME);
        onCreate(db);
    }

    public void insertEvent(Event event){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBEntry.COLUMN_NAME_TITLE, event.getTitle());
        values.put(DBContract.DBEntry.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(DBContract.DBEntry.COLUMN_NAME_DATE, event.getDate());
        values.put(DBContract.DBEntry.COLUMN_NAME_TIME, event.getTime());
        values.put(DBContract.DBEntry.COLUMN_NAME_NOTIFICATION, false);
        //db.insertWithOnConflict(DBContract.DBEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.insert(DBContract.DBEntry.TABLE_NAME, null, values);
        db.close();
    }
    public List<Event> getEvents(){
        List events = new ArrayList();
        events.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME,
                new String[]{
                DBContract.DBEntry.COLUMN_NAME_TITLE,
                DBContract.DBEntry.COLUMN_NAME_DESCRIPTION,
                DBContract.DBEntry.COLUMN_NAME_DATE,
                DBContract.DBEntry.COLUMN_NAME_TIME,
                DBContract.DBEntry.COLUMN_NAME_NOTIFICATION
                }, null, null, null, null, null
        );
        if(cursor != null && cursor.moveToFirst()){
            do{
                Event event = new Event(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                events.add(event);
            }while(cursor.moveToNext());
        }
        return events;
    }
    public List<Event> getEventsTitles(){
        List events = new ArrayList();
        events.clear();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME,
                new String[]{
                        DBContract.DBEntry.COLUMN_NAME_TITLE
                }, null, null, null, null, null
        );
        if(cursor != null && cursor.moveToFirst()){
            do{
                Event event = new Event(cursor.getString(0));
                events.add(event);
            }while(cursor.moveToNext());
        }
        return events;
    }
    public void updateEvent(Event event){
        String id = DBContract.DBEntry._ID;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.DBEntry.COLUMN_NAME_TITLE, event.getTitle());
        values.put(DBContract.DBEntry.COLUMN_NAME_DESCRIPTION, event.getDescription());
        values.put(DBContract.DBEntry.COLUMN_NAME_DATE, event.getDate());
        values.put(DBContract.DBEntry.COLUMN_NAME_TIME, event.getTime());
        values.put(DBContract.DBEntry.COLUMN_NAME_NOTIFICATION, false);
        db.update(DBContract.DBEntry.TABLE_NAME, values,  DBContract.DBEntry._ID + " = ?", new String[] { id } );
        db.close();
    }

}
