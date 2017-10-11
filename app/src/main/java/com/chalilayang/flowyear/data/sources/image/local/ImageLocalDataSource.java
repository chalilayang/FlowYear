package com.chalilayang.flowyear.data.sources.image.local;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.chalilayang.flowyear.data.entities.Image;
import com.chalilayang.flowyear.data.sources.image.ImagesDataSource;
import com.google.common.base.Preconditions;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chalilayang on 2016/11/11.
 */
public class ImageLocalDataSource implements ImagesDataSource {
    private static final int MSG_DATA_READY = 30;

    public static final int DATE_DESC = 1;
    public static final int DATE_ASC = 2;
    @IntDef({DATE_DESC, DATE_ASC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderMode {}

    private static ImageLocalDataSource INSTANCE;
    private List<Image> images;
    private Context context;

    private LoadImagesCallback callBack;
    private static InternalHandler sHandler;
    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_DATA_READY:
                    break;
                default:
                    break;
            }
        }
    }
    private static Handler getHandler() {
        synchronized (AsyncTask.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler();
            }
            return sHandler;
        }
    }
    public static ImageLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ImageLocalDataSource(context);
        }
        return INSTANCE;
    }

    private ImageLocalDataSource(Context context) {
        this.context = context;
        images = new ArrayList<>();
    }

    @Override
    public void getImages(@NonNull LoadImagesCallback callback) {
        this.callBack = Preconditions.checkNotNull(callback);
        task.execute();
    }

    private AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            loadImages(DATE_DESC);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (images.size() != 0) {
//                List<Image> result = new ArrayList<>(images.size());
//                Collections.copy(result, images);
//                callBack.onImagesLoaded(images);
            } else {
//                callBack.onDataNotAvailable();
            }
        }
    };

    private void loadImages( @OrderMode int order_type) {
        Preconditions.checkNotNull(context);
        String order = MediaStore.Images.Media.DATE_MODIFIED + " desc";
        switch (order_type) {
            case DATE_ASC:
                order = MediaStore.Images.Media.DATE_MODIFIED + " asc";
                break;
            case DATE_DESC:
                order = MediaStore.Images.Media.DATE_MODIFIED + " desc";
                break;
        }
        ContentResolver cr = context.getContentResolver();

        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[] {
                        "image/jpeg",
                        "image/jpg",
                        "image/png",
                        "image/bmp",
                        "image/webp"
                },
                order);


        if(null != cursor){
            while(cursor.moveToNext()){
                long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String filename = cursor.getString(
                        cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                );
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                String des = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));
                String latitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                if (!TextUtils.isEmpty(latitude)) {
                    double dd = Double.parseDouble(latitude)+0.0008;// + 0.0075;
                    latitude = String.valueOf(dd);
                }
                String longitude = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                if (!TextUtils.isEmpty(longitude)) {
                    double dd = Double.parseDouble(longitude)+0.006;// + 0.013;
                    longitude = String.valueOf(dd);
                }
                String bucket = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                Image data = new Image(path, filename, fileId, time);
                data.setBucketName(bucket);
                data.setLocation(latitude, longitude);
                data.setDescription(des);
                if (bucket.equalsIgnoreCase("Camera")) {
                    this.images.clear();
                    this.images.add(data);
                }
            }
            if(!cursor.isClosed()){
                cursor.close();
            }
        }
    }
}
