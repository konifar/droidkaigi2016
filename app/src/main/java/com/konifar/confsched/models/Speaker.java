package com.konifar.confsched.models;

import org.parceler.Parcel;

@Parcel
public class Speaker {

    int id;
    String name;
    String imageUrl;
    String twitterName;
    String githubName;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTwitterName() {
        return twitterName;
    }

    public String getGithubName() {
        return githubName;
    }

}
