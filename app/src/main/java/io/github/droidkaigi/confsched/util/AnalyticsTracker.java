package io.github.droidkaigi.confsched.util;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

public class AnalyticsTracker {

    final Tracker tracker;

    @Inject
    public AnalyticsTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public void sendScreenView(String screenName) {
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void sendEvent(String category, String action) {
        tracker.send(new HitBuilders.EventBuilder(category, action).build());
    }

}
