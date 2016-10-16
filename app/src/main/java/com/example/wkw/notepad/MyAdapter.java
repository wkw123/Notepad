package com.example.wkw.notepad;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by wkw on 2016/10/16.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private Cursor cursor;
    private View view;
    public MyAdapter(Context context, Cursor cursor) {
        this.cursor = cursor;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        view =  inflater.inflate(R.layout.item, null);
        TextView content_Text = (TextView) view.findViewById(R.id.list_content);
        TextView content_Time = (TextView) view.findViewById(R.id.list_time);
        ImageView content_Image = (ImageView) view.findViewById(R.id.list_image);
        ImageView content_Video = (ImageView) view.findViewById(R.id.list_video);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT));
        String time = cursor.getString(cursor.getColumnIndex(NoteDB.TIME));
        String ImageUrl = cursor.getString(cursor.getColumnIndex(NoteDB.IMAGE));
        String videoUrl = cursor.getString(cursor.getColumnIndex(NoteDB.VIDEO));
        content_Text.setText(content);
        content_Time.setText(time);
        content_Image.setImageBitmap(getImageThumbnail(ImageUrl, 200, 200));
        content_Video.setImageBitmap(getVideoThumbnail(videoUrl, 200, 200, MediaStore.Images.Thumbnails.MICRO_KIND));
        return view;
    }

    public Bitmap getImageThumbnail(String uri, int width, int height){
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(uri, options);
        options.inJustDecodeBounds = false;

        int beWidth = options.outWidth / width;
        int beHeight = options.outHeight / height;
        int be = 1;
        if (beWidth < beHeight){
            be = beWidth;
        }else{
            be = beHeight;
        }
        if(be <= 0){
            be = 1;
        }

        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(uri, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return  bitmap;
    }

    private Bitmap getVideoThumbnail(String uri, int width, int height, int kind){
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(uri,kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        return  bitmap;
    }

}
