package com.example.wkw.notepad;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
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
    private String imagePath;
    private String videoPath;
    private int mark;
    private MediaController mediaController;
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
        initView();
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

//
//        editor.putInt("num",1);
//        editor.commit();
//        Log.i("numss", settings.getInt("num", 10) + "");
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
        mark = 0;
        if (val.equals(TEXTFLAG)){
            mark = 1;
            content_Image.setVisibility(View.GONE);
            content_Video.setVisibility(View.GONE);
        }else if(val.equals(IMAGEFLAG)){


            content_Image.setVisibility(View.VISIBLE);
            content_Video.setVisibility(View.GONE);
            Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            imageIntent.addCategory(Intent.CATEGORY_DEFAULT);

            imagePath = Environment.getExternalStorageDirectory() +
                    "/" + getTime() + ".jpg";
            imageFile = new File(imagePath);
            Log.i("mains", imagePath);
            mark = 1;
      //      Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(imageFile));
        //    content_Image.setImageBitmap(bitmap);
            imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            startActivityForResult(imageIntent, 1);
//            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//            content_Image.setImageBitmap(bitmap);
        }else if(val.equals(VIDEOFLAG)){
            mark = 2;
            videoPath = Environment.getExternalStorageDirectory() +
                    "/" + getTime() + ".mp4";
            content_Image.setVisibility(View.GONE);
            content_Video.setVisibility(View.VISIBLE);
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoIntent.addCategory(Intent.CATEGORY_DEFAULT);
            videoFile = new File(videoPath);
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(videoFile));
            startActivityForResult(videoIntent, 2);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("mains","onActivityResult " + resultCode);
        Log.i("mains","mark " + mark);
        if(resultCode == -1) {
            if(mark == 1) {

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                content_Image.setImageBitmap(bitmap);

            }
            if(mark ==2) {
                Log.i("numss", videoPath);
                mediaController= new MediaController(this);
                //将videoView与mediaController建立关联
                content_Video.setMediaController(mediaController);
                //将mediaController与videoView建立关联
                mediaController.setMediaPlayer(content_Video);
                content_Video.setVideoPath(videoPath);
                content_Video.start();
            }

        }
    }
}
