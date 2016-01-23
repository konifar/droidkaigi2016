package com.konifar.confsched.util;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.Locale;

public class AppUtil {

    private static final String TWITTER_URL = "https://twitter.com/";
    private static final String GITHUB_URL = "https://github.com/";
    private static Locale locale;

    public static String getTwitterUrl(@NonNull String name) {
        return TWITTER_URL + name;
    }

    public static String getGitHubUrl(@NonNull String name) {
        return GITHUB_URL + name;
    }

    public static void resetLocale(Context context) {
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static Locale getLocale() {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public static void setLocale(String lang) {
        if (TextUtils.isEmpty(lang)) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(lang);
        }
    }

}
