package com.konifar.confsched.model;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;

@Parcel
@Table
public class Session {

    @PrimaryKey
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("title")
    public String title;

    @Column
    @SerializedName("description")
    public String description;

    @Column
    public int speakerId;

    @SerializedName("speaker")
    public Speaker speaker;

    @Column
    @SerializedName("stime")
    public Date sTime;

    @Column
    @SerializedName("etime")
    public Date eTime;

    @Column
    public int categoryId;

    @Nullable
    @SerializedName("category")
    public Category category;

    @Column
    public int placeId;

    @SerializedName("place")
    public Place place;

    @Column
    @SerializedName("language_id")
    public String languageId;

    @Column
    @Nullable
    @SerializedName("slide_url")
    public String slideUrl;

    public Session() {
    }

    public void prepareSave() {
        speakerId = speaker.id;
        if (category != null) categoryId = category.id;
        placeId = place.id;
    }

    public Category getCategory(OrmaDatabase orma) {
        if (category != null) {
            category = orma.selectFromCategory().idEq(categoryId).get(0);
        }
        return category;
    }

    public Place getPlace(OrmaDatabase orma) {
        if (place != null) {
            place = orma.selectFromPlace().idEq(placeId).get(0);
        }
        return place;
    }

    public Speaker getSpeaker(OrmaDatabase orma) {
        if (speaker != null) {
            speaker = orma.selectFromSpeaker().idEq(speakerId).get(0);
        }
        return speaker;
    }

}
