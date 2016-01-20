package com.konifar.confsched.util;

import android.support.annotation.NonNull;

public class AppUtil {

    private static final String TWITTER_URL = "https://twitter.com/";
    private static final String GITHUB_URL = "https://github.com/";

    public static String getTwitterUrl(@NonNull String name) {
        return TWITTER_URL + name;
    }

    public static String getGitHubUrl(@NonNull String name) {
        return GITHUB_URL + name;
    }

}
