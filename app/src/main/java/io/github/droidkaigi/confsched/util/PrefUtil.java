package io.github.droidkaigi.confsched.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PrefUtil {

    public static final String KEY_CURRENT_LANGUAGE_ID = "current_language_id";
    public static final String KEY_NOTIFICATION_SETTING = "notification_setting";
    public static final String KEY_SHOW_LOCAL_TIME = "show_local_time";

    private static SharedPreferences pref;

    public static SharedPreferences getPref(Context context) {
        if (pref == null) {
            pref = PreferenceManager.getDefaultSharedPreferences(context);
        }
        return pref;
    }

    public static void put(Context context, @NonNull String name, boolean value) {
        SharedPreferences.Editor edit = getPref(context).edit();
        edit.putBoolean(name, value);
        edit.apply();
    }

    public static void put(Context context, @NonNull String name, String value) {
        SharedPreferences.Editor edit = getPref(context).edit();
        edit.putString(name, value);
        edit.apply();
    }

    public static boolean contains(Context context, @NonNull String name) {
        return getPref(context).contains(name);
    }

    public static void remove(Context context, @NonNull String name) {
        SharedPreferences.Editor edit = getPref(context).edit();
        edit.remove(name);
        edit.apply();
    }

    public static String get(Context context, @NonNull String name, @Nullable String defaultValue) {
        return getPref(context).getString(name, defaultValue);
    }

    public static boolean get(Context context, @NonNull String name, boolean defaultValue) {
        return getPref(context).getBoolean(name, defaultValue);
    }
}

