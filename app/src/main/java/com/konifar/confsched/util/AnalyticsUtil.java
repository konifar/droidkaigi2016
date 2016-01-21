package com.konifar.confsched.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

public class AnalyticsUtil {

    final Tracker tracker;

    @Inject
    public AnalyticsUtil(Tracker tracker) {
        this.tracker = tracker;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void sendScreenView(String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(String category, String action) {
        tracker.send(new HitBuilders.EventBuilder(category, action).build());
    }

}
