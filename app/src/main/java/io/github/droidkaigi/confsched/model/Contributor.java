package io.github.droidkaigi.confsched.model;

import android.support.annotation.Nullable;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;
import com.google.gson.annotations.SerializedName;

@Table
public class Contributor {

    @PrimaryKey
    @Column("name")
    @SerializedName("login")
    public String name;

    @Column("avatar_url")
    @Nullable
    @SerializedName("avatar_url")
    public String avatarUrl;

    @Column("html_url")
    @Nullable
    @SerializedName("html_url")
    public String htmlUrl;

    @Column("contributions")
    @SerializedName("contributions")
    public int contributions;

}
