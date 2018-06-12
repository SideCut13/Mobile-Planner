package com.example.ania.mobileplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEvent extends AppCompatActivity{
    private EditText titleEvent;
    private EditText descriptionEvent;
    private Button addToDatabase;
    private DBHelper mDbHelper;
    private EditText dateEvent;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText timeEvent;
    private TimePickerDialog.OnTimeSetListener timePicker;
    private Switch notificationSwitch;
    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        Toast.makeText(getApplicationContext(), "before button", Toast.LENGTH_SHORT).show();
        titleEvent = findViewById(R.id.title_event);
        descriptionEvent = findViewById(R.id.description_event);
        addToDatabase = findViewById(R.id.add_database);
        mDbHelper = new DBHelper(this);
        notificationSwitch = findViewById(R.id.switch_notification);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
                                       @Override
                                       public void onAdLoaded() {
                                           super.onAdLoaded();
                                           mInterstitialAd.show();
                                       }
                                   }
        );

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Dodaj nowe wydarzenie");
        dateEvent = findViewById(R.id.date_event);
        calendar = Calendar.getInstance();
        datePicker = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        timeEvent = findViewById(R.id.time_event);
        timePicker = (view, hourOfDay, minute) -> {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            updateTimeLabel();
        };

        dateEvent.setOnClickListener(v -> new DatePickerDialog(AddEvent.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        timeEvent.setOnClickListener(v -> new TimePickerDialog(AddEvent.this, timePicker, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show());

        addToDatabase.setOnClickListener((event) -> addEventToDatabase());
    }
    private void updateDateLabel(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        dateEvent.setText(simpleDateFormat.format(calendar.getTime()));
    }
    private void updateTimeLabel(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("kk:mm", Locale.getDefault());
        timeEvent.setText(simpleDateFormat.format(calendar.getTime()));
    }
    private void addEventToDatabase(){
        String notification;
        if(notificationSwitch.isChecked()){
            notification = "1";
        }
        else{
            notification = "0";
        }

        Event event = new Event(titleEvent.getText().toString(), descriptionEvent.getText().toString(), dateEvent.getText().toString(), timeEvent.getText().toString(), notification);

        mDbHelper.insertEvent(event);
        Toast.makeText(getApplicationContext(), "add to database", Toast.LENGTH_SHORT).show();
    }

}
