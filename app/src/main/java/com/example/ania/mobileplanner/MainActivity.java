package com.example.ania.mobileplanner;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    CompactCalendarView compactCalendarView;
    private DBHelper mDbHelper;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat simpleDateFormatCurrent = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private Calendar calendar;
    private String date;
    private ListView listView;
    private TextView listText;
    private Button btnAddEvent;
   // private Button btnNotify;
    public Date dateClick;
    public String currentDate;
    com.example.ania.mobileplanner.Event event = null;

    public Date getDateClick() {
        return dateClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new DBHelper(this);
        calendar = Calendar.getInstance();
        date = simpleDateFormat.format(calendar.getTime());
        listView = findViewById(R.id.list_event);
        listText = findViewById(R.id.text_list_title);
        btnAddEvent = findViewById(R.id.button_add_event);
        currentDate = simpleDateFormatCurrent.format(calendar.getTime());
        //btnNotify = findViewById(R.id.button_notify);


        //action bar
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Dzisiaj: " + date);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        List<com.example.ania.mobileplanner.Event> events = mDbHelper.getEvents();
        for (int i = 0; i < events.size(); i++) {
            if(events.get(i).toString().contains("notification='1'")){
                event = events.get(i);
                Log.i("EVENT DATABASE", event.toString());
                if(event.getDate().equals(currentDate)){

                       // sendNotification();

                }
            }
        }
        //calendar
        Event event1= new Event(Color.rgb(29,171,167), 1477054800000L, "XXXXX");
        compactCalendarView.addEvent(event1);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();
                //String date = listText.setText().toSting();
                dateClick = dateClicked;
                Intent listIntent = new Intent(context, DailyListEvents.class);
                startActivity(listIntent);
                /*if(dateClicked.toString().compareTo("Fri Oct 21 09:00:00 AST 2016")==0){
                    Toast.makeText(context, "XXXX", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "No event", Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //tu trzeba zmienić na dany dzień żeby wyświetlało!!!!!!!!!!!
                if(simpleDateFormat.format(firstDayOfNewMonth).toString().equals(date)){
                    actionBar.setTitle("Dzisiaj: " + date);
                }
                else{
                    //actionBar.setTitle("Dzisiaj: " + date);
                    //może żeby wyświetlało tylko miesiąc ? albo date tego dnia z miesiąca?
                    //jak się przesunie to wtedy jest 1. czerwca 2018, amozę zrobić zeby było
                    //
                    actionBar.setTitle(simpleDateFormat.format(firstDayOfNewMonth));
                }
            }

        });
      /*  btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(v);
            }
        });*/
      btnAddEvent.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Context context = getApplicationContext();
              Intent addEventIntent = new Intent(context, AddEvent.class);
              startActivity(addEventIntent);
          }
      });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event_btn:
                Intent addEventIntent = new Intent(this, AddEvent.class);
                startActivity(addEventIntent);
                return true;
            /*case R.id.button_delete_event:
                mDbHelper.deleteEvent(findViewById(R.id.button_delete_event));
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void sendNotification(){
        Log.i("NOTIFICATION LOG", "I'm here");
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Mobile Planner")
                        .setContentText(event.getTitle() + " " + event.getTime()); //tutaj title z event database z danego dnia - godzina od godziny wydarzenia
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        AlarmManager alarmManager = (AlarmManager) getSystemService(
                ALARM_SERVICE);
        Calendar alarmStartTime = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 00);
        alarmStartTime.set(Calendar.MINUTE, 14);
        alarmStartTime.set(Calendar.SECOND, 0);

       /* alarmManager.set(AlarmManager.RTC_WAKEUP,//System.currentTimeMillis()+20*1000
                alarmStartTime.getTimeInMillis()
                , contentIntent);*/
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());

    }




}
