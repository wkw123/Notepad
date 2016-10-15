package com.example.wkw.notepad;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wkw on 2016/10/15.
 */

public class NoteDB extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "NOTES";
    public static final String CONTENT = "CONTENT";
    public static final String ID = "_ID";
    public static final String TIME = "TIME";

    public NoteDB(Context context) {
        super(context, "notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "" +
                "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CONTENT + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
