package com.chalilayang.flowyear.data.sources.image;

import android.support.annotation.NonNull;

import com.chalilayang.flowyear.data.entities.Image;

import java.util.List;

/**
 * Created by chalilayang on 2016/11/11.
 */

public interface ImagesDataSource {
    interface LoadImagesCallback {

        void onImagesLoaded(List<Image> images);

        void onDataNotAvailable();
    }

    interface GetImageCallback {

        void onImageLoaded(Image image);

        void onDataNotAvailable();
    }

    void getImages(@NonNull ImagesDataSource.LoadImagesCallback callback);
}
