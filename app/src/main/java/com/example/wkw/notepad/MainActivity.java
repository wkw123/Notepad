package com.example.wkw.notepad;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wkw.notepad.NoteDB;
import com.example.wkw.notepad.R;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private NoteDB noteDB;
    private SQLiteDatabase dbWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noteDB = new NoteDB(this);
        dbWrite = noteDB.getWritableDatabase();
        addDB();
    }

    public  void addDB(){
        ContentValues cv = new ContentValues();
        cv.put(NoteDB.CONTENT, "hello");
        cv.put(NoteDB.TIME, getTime());
        dbWrite.insert(NoteDB.TABLE_NAME, null, cv);
    }
    public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date currentDate = new Date();

        String time = format.format(currentDate);
        return  time;
    }
}
