package com.example.ania.mobileplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateEvent extends AppCompatActivity{
    private EditText updateTitleEvent;
    private EditText updateDescriptionEvent;
    private Button updateAddToDatabase;
    private DBHelper mDbHelper;
    private EditText updateDateEvent;
    private DatePickerDialog.OnDateSetListener updateDatePicker;
    private Calendar updateCalendar;
    private EditText updateTimeEvent;
    private TimePickerDialog.OnTimeSetListener updateTimePicker;
    private Switch updateNotificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_event);

        updateTitleEvent = findViewById(R.id.update_title_event);
        updateDescriptionEvent = findViewById(R.id.update_description_event);
        updateAddToDatabase = findViewById(R.id.update_add_database);
        mDbHelper = new DBHelper(this);
        updateNotificationSwitch = findViewById(R.id.update_switch_notification);


        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Edytuj wydarzenie");
        updateDateEvent = findViewById(R.id.update_date_event);
        updateCalendar = Calendar.getInstance();
        updateDatePicker = (view, year, month, dayOfMonth) -> {
            updateCalendar.set(Calendar.YEAR, year);
            updateCalendar.set(Calendar.MONTH, month);
            updateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        updateTimeEvent = findViewById(R.id.update_time_event);
        updateTimePicker = (view, hourOfDay, minute) -> {
            updateCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            updateCalendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        };

        updateDateEvent.setOnClickListener(v -> new DatePickerDialog(UpdateEvent.this, updateDatePicker, updateCalendar.get(Calendar.YEAR), updateCalendar.get(Calendar.MONTH), updateCalendar.get(Calendar.DAY_OF_MONTH)).show());

        updateTimeEvent.setOnClickListener(v -> new TimePickerDialog(UpdateEvent.this, updateTimePicker, updateCalendar.get(Calendar.HOUR_OF_DAY), updateCalendar.get(Calendar.MINUTE), true).show());
     //   updateAddToDatabase.setOnClickListener((event) -> updateEventToDatabase());
    }
    private void updateDateLabel(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        updateDateEvent.setText(simpleDateFormat.format(updateCalendar.getTime()));
    }
    private void updateTimeLabel(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
        updateTimeEvent.setText(simpleDateFormat.format(updateCalendar.getTime()));
    }
    private void updateEventToDatabase(){
        String notification;
        if(updateNotificationSwitch.isChecked()){
            notification = "true";
        }
        else{
            notification = "false";
        }
       // Event event = new Event(updateTitleEvent.getText().toString(), updateDescriptionEvent.getText().toString(), updateDateEvent.getText().toString(), updateTimeEvent.getText().toString(), notification);

      //  mDbHelper.updateEvent(event);
    }
}
