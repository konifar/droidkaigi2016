package com.konifar.confsched.models;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

@Parcel
public class Session {

    int id;
    String title;
    Speaker speaker;
    Date sTime;
    Date eTime;
    List<Category> categories;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public Date getsTime() {
        return sTime;
    }

    public Date geteTime() {
        return eTime;
    }

    public List<Category> getCategories() {
        return categories;
    }

}
