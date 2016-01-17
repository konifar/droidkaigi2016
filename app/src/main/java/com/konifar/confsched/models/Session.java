package com.konifar.confsched.models;

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
    @SerializedName("s_time")
    public Date sTime;
    @SerializedName("e_time")
    public Date eTime;
    @SerializedName("categories")
    public List<Category> categories;

    public Session() {
    }

}
