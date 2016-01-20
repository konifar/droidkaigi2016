package com.konifar.confsched.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Category {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;

    public Category() {
    }
}
