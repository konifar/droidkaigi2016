package io.github.droidkaigi.confsched.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import java.util.Date;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.BR;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AlarmUtil;
import io.github.droidkaigi.confsched.util.DateUtil;
import io.github.droidkaigi.confsched.util.PageNavigator;
import io.github.droidkaigi.confsched.viewmodel.event.EventBus;
import io.github.droidkaigi.confsched.viewmodel.event.SessionSelectedChangedEvent;

public class SessionDetailViewModel extends BaseObservable implements ViewModel {

    private final Context context;
    private final PageNavigator navigator;
    private final SessionDao dao;
    private final EventBus eventBus;
    private final AlarmUtil alarmUtil;

    @Bindable
    public Session session;
    @Bindable
    public ObservableInt fabRippleColorResId;

    @Inject
    public SessionDetailViewModel(Context context,
                                  PageNavigator navigator,
                                  SessionDao dao,
                                  EventBus eventBus,
                                  AlarmUtil alarmUtil) {
        this.context = context;
        this.navigator = navigator;
        this.dao = dao;
        this.eventBus = eventBus;
        this.alarmUtil = alarmUtil;
    }

    public void setSession(Session session) {
        this.session = session;
        this.fabRippleColorResId = new ObservableInt(ContextCompat.getColor(context, session.category.getPaleColorResId()));
        notifyPropertyChanged(BR.session);
        notifyPropertyChanged(BR.fabRippleColorResId);
    }

    @Override
    public void destroy() {
        // Do nothing
    }

    public boolean shouldShowShareMenuItem() {
        return !TextUtils.isEmpty(session.shareUrl);
    }

    public void onClickShareMenuItem() {
        if (shouldShowShareMenuItem()) {
            navigator.showShareChooser(session.shareUrl);
        }
    }

    public void onClickFeedbackButton(@SuppressWarnings("unused") View view) {
        navigator.showFeedback(session);
    }

    public void onClickSlideIcon(@SuppressWarnings("unused") View view) {
        if (session.hasSlide()) {
            navigator.showBrowser(session.slideUrl);
        }
    }

    public void onClickMovieIcon(@SuppressWarnings("unused") View view) {
        if (session.hasDashVideo()) {
            navigator.showVideoPlayer(session);
        }
    }

    public void onClickFab(@SuppressWarnings("unused") View view) {
        session.checked = !session.checked;
        notifyPropertyChanged(BR.session);
        dao.updateChecked(session);
        eventBus.post(new SessionSelectedChangedEvent(session));
        alarmUtil.handleSessionAlarm(session);
    }

    public String sessionTimeRange() {
        Date displaySTime = session.getDisplaySTime(context);
        Date displayETime = session.getDisplayETime(context);

        return context.getString(R.string.session_time_range,
                DateUtil.getLongFormatDate(displaySTime, context),
                DateUtil.getHourMinute(displayETime),
                DateUtil.getMinutes(displaySTime, displayETime));
    }

    public int slideIconVisibility() {
        return session.hasSlide() ? View.VISIBLE : View.GONE;
    }

    public int dashVideoIconVisibility() {
        return session.hasDashVideo() ? View.VISIBLE : View.GONE;
    }

}
