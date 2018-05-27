package com.example.ania.mobileplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyListEvents extends AppCompatActivity{
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private DBHelper mDbHelper;
    private Button buttonDelete;
    private Button buttonUpdate;
    private EditText dateEvent;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText timeEvent;
    private TimePickerDialog.OnTimeSetListener timePicker;
    private Switch notificationSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_event_list);
        mDbHelper = new DBHelper(this);
        listView = findViewById(R.id.list_event);
        buttonDelete = findViewById(R.id.button_delete_event);
        buttonDelete.setOnClickListener(v -> deleteEvent(v));




        List events = mDbHelper.getEventsTitles();//getEvents();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.daily_event_list, R.id.text_list_title, events);
        listView.setAdapter(arrayAdapter);
    }
    public void deleteEvent(View view){
        View parentView = (View) view.getParent();
        TextView titleTextView = parentView.findViewById(R.id.text_list_title);
        String event = String.valueOf(titleTextView.getText());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry.COLUMN_NAME_TITLE + " = ? ", new String[]{event});
        db.close();
        updateView();
    }
    private void updateView() {
        List<String> events = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.DBEntry.TABLE_NAME,
                new String[]{DBContract.DBEntry._ID, DBContract.DBEntry.COLUMN_NAME_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DBContract.DBEntry.COLUMN_NAME_TITLE);
            events.add(cursor.getString(idx));
        }

        if (arrayAdapter == null) {
            arrayAdapter = new ArrayAdapter<>(this,
                    R.layout.daily_event_list,
                    R.id.text_list_title,
                    events);
            listView.setAdapter(arrayAdapter);
        } else {
            arrayAdapter.clear();
            arrayAdapter.addAll(events);
            arrayAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }
   /* public void updateEvent(){
        View parentView = (View) view.getParent();
        TextView titleTextView = parentView.findViewById(R.id.text_list_title);
        String event = String.valueOf(titleTextView.getText());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(DBContract.DBEntry.TABLE_NAME, DBContract.DBEntry.COLUMN_NAME_TITLE + " = ? ", new String[]{event});
        db.close();
        updateView();
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
    }*/

}
