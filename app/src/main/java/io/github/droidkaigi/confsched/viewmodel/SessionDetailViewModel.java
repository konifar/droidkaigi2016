package io.github.droidkaigi.confsched.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.PageNavigator;

public class SessionDetailViewModel implements ViewModel {

    private final Context context;
    private final PageNavigator navigator;
    private final SessionDao dao;

    public Session session;

    @Inject
    public SessionDetailViewModel(Context context, PageNavigator navigator, SessionDao dao) {
        this.context = context;
        this.navigator = navigator;
        this.dao = dao;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void destroy() {
        // TODO
    }

    public void onClickShareMenuItem() {
        if (!TextUtils.isEmpty(session.shareUrl)) {
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

}
