package com.example.ania.mobileplanner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddEvent extends AppCompatActivity{
    private EditText titleEvent;
    private EditText descriptionEvent;
    private Button addToDatabase;
    private DBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        Toast.makeText(getApplicationContext(), "before button", Toast.LENGTH_SHORT).show();
        titleEvent = findViewById(R.id.title_event);
        descriptionEvent = findViewById(R.id.description_event);
        addToDatabase = findViewById(R.id.add_database);
        mDbHelper = new DBHelper(this);



        addToDatabase.setOnClickListener((event) ->{
            addEventToDatabase();
        });
        Toast.makeText(getApplicationContext(), "after button", Toast.LENGTH_SHORT).show();
    }
    private void addEventToDatabase(){
        Event event = new Event(titleEvent.getText().toString(), descriptionEvent.getText().toString(), "5-12-2018", "10:00", "false");

        mDbHelper.insertEvent(event);
        Toast.makeText(getApplicationContext(), "add to database", Toast.LENGTH_SHORT).show();
    }
}
