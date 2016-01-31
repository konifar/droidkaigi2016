package com.konifar.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.konifar.confsched.model.Session;

import javax.inject.Singleton;

@Singleton
public class ActivityNavigator {

    public void showSessionDetail(@NonNull Activity activity, @NonNull Session session, int requestCode) {
        SessionDetailActivity.startForResult(activity, session, requestCode);
    }

    public void showMain(@NonNull Activity activity) {
        MainActivity.start(activity);
    }

    public void showWebView(@NonNull Context context, @NonNull String url, @NonNull String title) {
        WebViewActivity.start(context, url, title);
    }

}
