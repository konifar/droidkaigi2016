package io.github.droidkaigi.confsched.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import io.github.droidkaigi.confsched.R;

public class SearchResult {

    public String text;

    @DrawableRes
    public int iconRes;

    @StringRes
    public int typeRes;

    public Session session;

    private SearchResult(String text, int iconRes, int typeRes, Session session) {
        this.text = text;
        this.iconRes = iconRes;
        this.typeRes = typeRes;
        this.session = session;
    }

    public static SearchResult createTitleType(@NonNull Session session) {
        return new SearchResult(session.title, R.drawable.ic_event_note_grey_600_12dp, R.string.title, session);
    }

    public static SearchResult createDescriptionType(@NonNull Session session) {
        return new SearchResult(session.description, R.drawable.ic_description_grey_600_12dp, R.string.description, session);
    }

    public static SearchResult createSpeakerType(@NonNull Session session) {
        return new SearchResult(session.speaker.name, R.drawable.ic_person_grey_600_12dp, R.string.speaker, session);
    }

}
