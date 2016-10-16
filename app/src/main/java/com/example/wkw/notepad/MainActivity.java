package com.example.wkw.notepad;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.wkw.notepad.NoteDB;
import com.example.wkw.notepad.R;

import java.util.Date;


import static com.example.wkw.notepad.Flag.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button textBtn;
    private Button imgBtn;
    private Button videoBtn;
    private ListView listView;
    private Intent intent;

    private MyAdapter myAdapter;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;

//    private NoteDB noteDB;
//    private SQLiteDatabase dbWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        noteDB = new NoteDB(this);
//        dbWrite = noteDB.getWritableDatabase();
//        addDB();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllDb();
    }

    //    public  void addDB(){
//        ContentValues cv = new ContentValues();
//        cv.put(NoteDB.CONTENT, "hello");
//        cv.put(NoteDB.TIME, getTime());
//        dbWrite.insert(NoteDB.TABLE_NAME, null, cv);
//    }
//    public String getTime(){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
//        Date currentDate = new Date();
//
//        String time = format.format(currentDate);
//        return  time;
//    }

    public void initView(){
        listView = (ListView) findViewById(R.id.listview);
        textBtn = (Button) findViewById(R.id.text);
        imgBtn = (Button) findViewById(R.id.image);
        videoBtn = (Button) findViewById(R.id.video);
        textBtn.setOnClickListener(this);
        imgBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);

        noteDB = new NoteDB(this);
        dbReader = noteDB.getReadableDatabase();
    }

    @Override
    public void onClick(View v) {
        intent = new Intent(this, AddContent.class);
        switch (v.getId()){

            case R.id.text:

                intent.putExtra("flag", TEXTFLAG);
                startActivity(intent);
                break;
            case R.id.image:

                intent.putExtra("flag", IMAGEFLAG);
                startActivity(intent);
                break;
            case R.id.video:

                intent.putExtra("flag", VIDEOFLAG);
                startActivity(intent);
                break;
        }
    }

    public void getAllDb(){
        Cursor cursor = dbReader.query(NoteDB.TABLE_NAME, null, null, null, null, null, null);
        myAdapter = new MyAdapter(this, cursor);
        listView.setAdapter(myAdapter);
    }

}
