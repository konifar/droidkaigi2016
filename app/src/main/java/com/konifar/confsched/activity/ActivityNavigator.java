package com.konifar.confsched.activity;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.konifar.confsched.model.Session;

import javax.inject.Singleton;

@Singleton
public class ActivityNavigator {

    public void showSessionDetail(@NonNull Activity activity, @NonNull Session session) {
        SessionDetailActivity.start(activity, session);
    }

}
