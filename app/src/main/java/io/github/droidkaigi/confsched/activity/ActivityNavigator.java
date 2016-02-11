package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import io.github.droidkaigi.confsched.model.Session;

@Singleton
public class ActivityNavigator {

    public void showSessionDetail(@NonNull Activity activity, @NonNull Session session, int requestCode) {
        SessionDetailActivity.startForResult(activity, session, requestCode);
    }

    public void showMain(@NonNull Activity activity, boolean shouldRefresh) {
        MainActivity.start(activity, shouldRefresh);
    }

    public void showWebView(@NonNull Context context, @NonNull String url, @NonNull String title) {
        WebViewActivity.start(context, url, title);
    }

    public void showSearch(@NonNull Activity activity) {
        SearchActivity.start(activity);
    }

    public void showFeedback(@NonNull Context context, @NonNull Session session) {
        SessionFeedbackActivity.start(context, session);
    }

}
