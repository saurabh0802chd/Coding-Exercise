package com.example.saurabh.codingexercise;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Saurabh on 8/14/2016.
 * Class represents a simple object of data received as JSON
 */

public class Entry implements Parcelable {
    private String imageUrl = null;
    private String title = null;
    private String description = null;

    Entry(JSONObject data) {
        try {
            imageUrl = data.getString("image");
            title = data.getString("title");
            description = data.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Entry(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imageUrl);
        parcel.writeString(title);
        parcel.writeString(description);
    }

    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Entry createFromParcel(Parcel in) {
                    return new Entry(in);
                }

                public Entry[] newArray(int size) {
                    return new Entry[size];
                }
            };
}
