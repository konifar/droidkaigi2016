package io.github.droidkaigi.confsched.prefs;

import com.rejasupotaro.android.kvs.annotations.Key;
import com.rejasupotaro.android.kvs.annotations.Table;

@Table(name = "io.github.droidkaigi.confsched_preferences")
public interface DefaultPrefsSchema {
    @Key(name = "current_language_id")
    String languageId = "";
    @Key(name = "notification_setting")
    boolean notificationFlag = true;
    @Key(name = "heads_up_setting")
    boolean headsUpFlag = true;
    @Key(name = "show_local_time")
    boolean showLocalTimeFlag = false;
}
