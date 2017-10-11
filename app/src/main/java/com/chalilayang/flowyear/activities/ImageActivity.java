package com.chalilayang.flowyear.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.chalilayang.flowyear.R;

import java.io.FileNotFoundException;

public class ImageActivity extends AppCompatActivity {
    private static final String TAG = "ImageActivity";
    private Bitmap bmp;
    private ImageView imageView;
    private SeekBar seekBarR;
    private SeekBar seekBarG;
    private SeekBar seekBarB;
    private SeekBar seekBarX;
    private float mR = 1;
    private float mG = 1;
    private float mB = 1;
    private float mX = 1;
    private ColorMatrix colorMatrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        colorMatrix = new ColorMatrix();
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0 );
            }
        });
        imageView = (ImageView)findViewById(R.id.image);
        colorMatrix.setScale(mR, mG, mB, mX);
        imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        seekBarR = (SeekBar) findViewById(R.id.seekBarR);
        seekBarR.setProgress(0);
        seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rate = progress / 100.0f;
                mR = 2 * rate;
                colorMatrix.setScale(mR, mG, mB, mX);
                Log.i(TAG, "onProgressChanged: " + mR + " " + mG + " " + mB + " " + mX);
                imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarG = (SeekBar) findViewById(R.id.seekBarG);
        seekBarG.setProgress(0);
        seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rate = progress / 100.0f;
                mG = 2 * rate;
                colorMatrix.setScale(mR, mG, mB, mX);
                Log.i(TAG, "onProgressChanged: " + mR + " " + mG + " " + mB + " " + mX);
                imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarB = (SeekBar) findViewById(R.id.seekBarB);
        seekBarB.setProgress(0);
        seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rate = progress / 100.0f;
                mB = 2 * rate;
                colorMatrix.setScale(mR, mG, mB, mX);
                Log.i(TAG, "onProgressChanged: " + mR + " " + mG + " " + mB + " " + mX);
                imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarX = (SeekBar) findViewById(R.id.seekBarX);
        seekBarX.setProgress(0);
        seekBarX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rate = progress / 100.0f;
                mX = 2 * rate;
                colorMatrix.setScale(mR, mG, mB, mX);
                Log.i(TAG, "onProgressChanged: " + mR + " " + mG + " " + mB + " " + mX);
                imageView.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            ShowPhotoByImageView(data);
            seekBarR.setProgress(50);
            seekBarG.setProgress(50);
            seekBarB.setProgress(50);
            seekBarX.setProgress(50);
        }
    }

    public void ShowPhotoByImageView(Intent data) {
        Uri imageFileUri = data.getData();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        try {
            BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
            bmpFactoryOptions.inJustDecodeBounds = true;
            bmp = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
            int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
            int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
            if(heightRatio>1&&widthRatio>1) {
                if(heightRatio>widthRatio) {
                    bmpFactoryOptions.inSampleSize = heightRatio*2;
                }
                else {
                    bmpFactoryOptions.inSampleSize = widthRatio*2;
                }
            }
            bmpFactoryOptions.inJustDecodeBounds = false;
            bmp = BitmapFactory.decodeStream(
                    getContentResolver().openInputStream(imageFileUri), null, bmpFactoryOptions);
            imageView.setImageBitmap(bmp);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
