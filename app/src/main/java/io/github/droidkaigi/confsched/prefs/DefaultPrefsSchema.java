package io.github.droidkaigi.confsched.prefs;

import android.content.Context;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table("io.github.droidkaigi.confsched_preferences")
public class DefaultPrefsSchema {
    @Key("current_language_id")
    String languageId;
    @Key("notification_setting")
    boolean notificationFlag;
    @Key("heads_up_setting")
    boolean headsUpFlag;
    @Key("show_local_time")
    boolean showLocalTimeFlag;

    private static DefaultPrefs prefs;

    public static synchronized DefaultPrefs get(Context context) {
        if (prefs == null) {
            prefs = new DefaultPrefs(context);
        }
        return prefs;
    }
}
