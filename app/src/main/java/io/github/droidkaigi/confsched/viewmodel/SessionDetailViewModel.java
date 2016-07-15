package io.github.droidkaigi.confsched.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AlarmUtil;
import io.github.droidkaigi.confsched.util.PageNavigator;
import io.github.droidkaigi.confsched.viewmodel.event.EventBus;
import io.github.droidkaigi.confsched.viewmodel.event.SessionSelectedChangedEvent;

public class SessionDetailViewModel implements ViewModel {

    private final Context context;
    private final PageNavigator navigator;
    private final SessionDao dao;
    private final EventBus eventBus;

    public Session session;

    @Inject
    public SessionDetailViewModel(Context context,
                                  PageNavigator navigator,
                                  SessionDao dao,
                                  EventBus eventBus) {
        this.context = context;
        this.navigator = navigator;
        this.dao = dao;
        this.eventBus = eventBus;
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

    public void onClickFab(View fab) {
        boolean checked = !fab.isSelected();
        fab.setSelected(checked);
        session.checked = checked;
        dao.updateChecked(session);
        // TODO This is not smart way. I want to solve by using two way binding.
        eventBus.post(new SessionSelectedChangedEvent(session));
        AlarmUtil.handleSessionAlarm(context, session);
    }

}
