package com.konifar.confsched.model;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
@Table
public class Speaker {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("name")
    public String name;

    @Column
    @Nullable
    @SerializedName("image_url")
    public String imageUrl;

    @Column
    @Nullable
    @SerializedName("twitter_name")
    public String twitterName;

    @Column
    @Nullable
    @SerializedName("github_name")
    public String githubName;

}
