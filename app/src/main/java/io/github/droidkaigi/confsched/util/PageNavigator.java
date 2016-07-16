package io.github.droidkaigi.confsched.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.MainActivity;
import io.github.droidkaigi.confsched.activity.SearchActivity;
import io.github.droidkaigi.confsched.activity.SessionDetailActivity;
import io.github.droidkaigi.confsched.activity.SessionFeedbackActivity;
import io.github.droidkaigi.confsched.activity.VideoPlayerActivity;
import io.github.droidkaigi.confsched.activity.WebViewActivity;
import io.github.droidkaigi.confsched.di.scope.ActivityScope;
import io.github.droidkaigi.confsched.model.Session;

@ActivityScope
public class PageNavigator {

    private static final String HASH_TAG = "#droidkaigi";

    AppCompatActivity activity;

    @Inject
    public PageNavigator(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void showSessionDetail(@NonNull Session session) {
        Intent intent = SessionDetailActivity.createIntent(activity, session);
        activity.startActivity(intent);
    }

    public void showMain(@NonNull Activity activity, boolean shouldRefresh) {
        MainActivity.start(activity, shouldRefresh);
    }

    public void showWebView(@NonNull Context context, @NonNull String url, @NonNull String title) {
        WebViewActivity.start(context, url, title);
    }

    public void showSearch(@NonNull Fragment fragment, int requestCode) {
        SearchActivity.start(fragment, requestCode);
    }

    public void showFeedback(@NonNull Session session) {
        SessionFeedbackActivity.start(activity, session);
    }

    public void showVideoPlayer(@NonNull Session session) {
        Intent intent = VideoPlayerActivity.createIntent(activity, session.movieDashUrl);
        activity.startActivity(intent);
    }

    public void showShareChooser(String url) {
        if (TextUtils.isEmpty(url)) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, url + " " + HASH_TAG);
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share)));
    }

    public void showBrowser(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

}
