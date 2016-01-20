package com.konifar.confsched.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

@Parcel
public class Session {

    @SerializedName("id")
    public int id;
    @SerializedName("title")
    public String title;
    @SerializedName("speaker")
    public Speaker speaker;
    @SerializedName("stime")
    public Date sTime;
    @SerializedName("etime")
    public Date eTime;
    @SerializedName("categories")
    public List<Category> categories;
    @SerializedName("place")
    public Place place;

    public Session() {
    }

}
