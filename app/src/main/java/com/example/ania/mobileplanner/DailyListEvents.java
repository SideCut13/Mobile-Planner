package com.example.ania.mobileplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DailyListEvents extends AppCompatActivity{
    private ListView listView;
    private ArrayAdapter arrayAdapter;
    private DBHelper mDbHelper;
    private Button buttonDelete;
    private Button buttonUpdate;
    private Calendar calendar;
    private String currentDate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_event_list);
        mDbHelper = new DBHelper(this);
        listView = findViewById(R.id.list_event);
        buttonDelete = findViewById(R.id.button_delete_event);
        buttonUpdate = findViewById(R.id.button_update_event);
        calendar = Calendar.getInstance();
        currentDate = simpleDateFormat.format(calendar.getTime());


        buttonDelete.setOnClickListener(v -> deleteEvent(v));
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               updateEvent(v);
            }
        });

        List<Event> events = mDbHelper.getEvents();
        List eventsToDisplay = new ArrayList();
        for (int i = 0; i < events.size(); i++) {
           if(events.get(i).toString().contains(currentDate)){
               Event event = events.get(i);
               eventsToDisplay.add(event.getTitle());
               Log.i("EVENT", event.toString());
              if(events.toString().contains(event.getTitle())){

              }
           }
        }
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.daily_event_list, R.id.text_list_title, eventsToDisplay);
        listView.setAdapter(arrayAdapter);
       //List events = mDbHelper.getEventsTitles();//getEvents();
       // arrayAdapter = new ArrayAdapter<String>(this, R.layout.daily_event_list, R.id.text_list_title, events);
       // listView.setAdapter(arrayAdapter);
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
