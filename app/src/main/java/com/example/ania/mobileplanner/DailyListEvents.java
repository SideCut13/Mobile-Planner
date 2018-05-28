package com.example.ania.mobileplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyListEvents extends AppCompatActivity{
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private DBHelper mDbHelper;
    private Button buttonDelete;
    private Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_event_list);
        mDbHelper = new DBHelper(this);
        listView = findViewById(R.id.list_event);
        buttonDelete = findViewById(R.id.button_delete_event);
        buttonUpdate = findViewById(R.id.button_update_event);


        buttonDelete.setOnClickListener(v -> deleteEvent(v));
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateEvent(v);
            }
        });

        List events = mDbHelper.getEventsTitles();//getEvents();
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.daily_event_list, R.id.text_list_title, events);
        listView.setAdapter(arrayAdapter);
    }
    public void updateEvent(View view){
        //View parentView = (View) view.getParent();
        //TextView titleTextView = parentView.findViewById(R.id.text_list_title);
        Context context = getApplicationContext();
        Intent updateEventIntent = new Intent(context, UpdateEvent.class);
        startActivity(updateEventIntent);
        Toast.makeText(getApplicationContext(), "update1", Toast.LENGTH_SHORT).show();

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


}
