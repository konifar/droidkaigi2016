package io.github.droidkaigi.confsched.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.activity.PageNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.model.Session;

public class SessionDetailViewModel implements ViewModel {

    private final Context context;
    private final PageNavigator navigator;
    private final SessionDao dao;

    private Session session;

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

    public void onClickSlideIcon() {
        if (session.hasSlide()) {
            navigator.showBrowser(session.slideUrl);
        }
    }

    public void onClickMovieIcon() {
        if (session.hasDashVideo()) {
            navigator.showVideoPlayer(session);
        }
    }

}
