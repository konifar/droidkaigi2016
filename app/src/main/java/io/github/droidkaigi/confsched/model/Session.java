package io.github.droidkaigi.confsched.model;

import com.google.gson.annotations.SerializedName;

import com.github.gfx.android.orma.annotation.Column;
import com.github.gfx.android.orma.annotation.PrimaryKey;
import com.github.gfx.android.orma.annotation.Table;

import org.parceler.Parcel;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.Date;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.util.LocaleUtil;

@Parcel
@Table
public class Session {

    @PrimaryKey(auto = false)
    @Column(indexed = true)
    @SerializedName("id")
    public int id;

    @Column(indexed = true)
    @SerializedName("title")
    public String title;

    @Column
    @SerializedName("description")
    public String description;

    @Column(indexed = true)
    @SerializedName("speaker")
    public Speaker speaker;

    @Column
    @SerializedName("stime")
    public Date stime;

    @Column
    @SerializedName("etime")
    public Date etime;

    @Column(indexed = true)
    @Nullable
    @SerializedName("category")
    public Category category;

    @Column(indexed = true)
    @SerializedName("place")
    public Place place;

    @Column
    @SerializedName("language_id")
    public String languageId;

    @Column
    @Nullable
    @SerializedName("slide_url")
    public String slideUrl;

    @Column
    @Nullable
    @SerializedName("movie_url")
    public String movieUrl;

    @Column
    @Nullable
    @SerializedName("movie_dash_url")
    public String movieDashUrl;

    @Column
    @Nullable
    @SerializedName("share_url")
    public String shareUrl;

    @Column(indexed = true)
    public boolean checked;

    public Session() {
    }

    /***
     * The copy constructor
     * @param session copy from
     */
    public Session(Session session) {
        id = session.id;
        title = session.title;
        description = session.description;
        speaker = session.speaker;
        stime = session.stime;
        etime = session.etime;
        category = session.category;
        place = session.place;
        languageId = session.languageId;
        slideUrl = session.slideUrl;
        movieUrl = session.movieUrl;
        movieDashUrl = session.movieDashUrl;
        shareUrl = session.shareUrl;
        checked = session.checked;
    }

    public Date getDisplaySTime(Context context) {
        return LocaleUtil.getDisplayDate(stime, context);
    }

    public Date getDisplayETime(Context context) {
        return LocaleUtil.getDisplayDate(etime, context);
    }

    public int getLanguageResId() {
        switch (languageId) {
            case LocaleUtil.LANG_EN_ID:
                return R.string.lang_en;
            case LocaleUtil.LANG_JA_ID:
                return R.string.lang_ja;
            default:
                return R.string.lang_en;
        }
    }

    public boolean shouldNotify(long remindDuration) {
        Date now = LocaleUtil.getConfTimezoneCurrentDate();
        long diff = stime.getTime() - now.getTime();
        return remindDuration < diff;
    }

    public boolean hasSlide() {
        return !TextUtils.isEmpty(slideUrl);
    }

    public boolean hasDashVideo() {
        return !TextUtils.isEmpty(movieDashUrl);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Session && ((Session) o).id == id || super.equals(o);
    }

    @Override
    public int hashCode() {
        return id;
    }
}
