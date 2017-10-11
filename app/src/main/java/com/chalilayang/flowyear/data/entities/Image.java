/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chalilayang.flowyear.data.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Immutable model class for a Image.
 */
public final class Image implements Parcelable {

    private String filePath;
    private String title;
    private long timeStamp;
    private long filedId = -1;
    private String description;
    private String latitude;
    private String longitude;
    private String bucketName;

    public Image(@NonNull String path, @NonNull String mTitle, long filedId, long timeStamp) {
        this.filePath = path;
        this.title = mTitle;
        this.filedId = filedId;
        this.timeStamp = timeStamp;
    }
    public void setLocation(String lati, String lonti) {
        this.latitude = lati;
        this.longitude = lonti;
    }
    public void setBucketName(String name) {
        this.bucketName = name;
    }
    public String getLatitude() {
        return this.latitude;
    }
    public String getLongitude() {
        return this.longitude;
    }
    public String getBucketName() {
        return this.bucketName;
    }
    public void setDescription(String des) {
        this.description = des;
    }
    public String getDescription() {
        return this.description;
    }
    public String getFilePath() {
        return this.filePath;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filePath);
        dest.writeString(this.title);
        dest.writeLong(this.timeStamp);
        dest.writeLong(this.filedId);
        dest.writeString(this.description);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.bucketName);
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[0];
        }
    };

    private Image(Parcel in) {
        this.filePath = in.readString();
        this.title = in.readString();
        this.timeStamp = in.readLong();
        this.filedId = in.readLong();
        this.description = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.bucketName = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
}
