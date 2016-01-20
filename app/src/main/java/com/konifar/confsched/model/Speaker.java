package com.konifar.confsched.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Speaker {

    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("image_url")
    public String imageUrl;
    @SerializedName("twitter_name")
    public String twitterName;
    @SerializedName("github_name")
    public String githubName;

}
