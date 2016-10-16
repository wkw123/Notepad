package com.example.wkw.notepad;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.example.wkw.notepad.Flag.*;
/**
 * Created by wkw on 2016/10/16.
 */

public class AddContent extends AppCompatActivity implements View.OnClickListener{

    private String val;
    private Button saveButton;
    private Button cancelButton;
    private EditText editText;
    private ImageView content_Image;
    private VideoView content_Video;
    private NoteDB noteDB;
    private SQLiteDatabase dbWrite;
    private File imageFile;
    private File videoFile;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.addcontent);

        val = getIntent().getStringExtra("flag");
        saveButton = (Button) findViewById(R.id.save);
        cancelButton = (Button) findViewById(R.id.cancel);
        editText = (EditText) findViewById(R.id.content_edittext);
        content_Image = (ImageView) findViewById(R.id.content_image);
        content_Video = (VideoView) findViewById(R.id.content_video);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        noteDB = new NoteDB(this);
        dbWrite = noteDB.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save:
                addDB();
                finish();
                break;

            case R.id.cancel:
                finish();
                break;
        }
    }

    private void addDB(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDB.CONTENT, editText.getText().toString());
        contentValues.put(NoteDB.TIME, getTime());
        contentValues.put(NoteDB.IMAGE, imageFile + "");
        contentValues.put(NoteDB.VIDEO, videoFile + "");
        dbWrite.insert(NoteDB.TABLE_NAME, null, contentValues);
    }

        public String getTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date currentDate = new Date();

        String time = format.format(currentDate);
        return  time;
    }

    public void initView(){
        if (val.equals(TEXTFLAG)){
            content_Image.setVisibility(View.GONE);
            content_Video.setVisibility(View.GONE);
        }else if(val.equals(IMAGEFLAG)){
            content_Image.setVisibility(View.VISIBLE);
            content_Video.setVisibility(View.GONE);
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
            "/" + getTime() + ".jpg");
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(imageIntent, 1);
        }else {
            content_Image.setVisibility(View.VISIBLE);
            content_Video.setVisibility(View.GONE);
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/" + getTime() + ".mp4 ");
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(videoIntent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            content_Image.setImageBitmap(bitmap);
        }else  if(resultCode ==2){
            content_Video.setVideoURI(Uri.fromFile(videoFile));
            content_Video.start();
        }
    }
}
