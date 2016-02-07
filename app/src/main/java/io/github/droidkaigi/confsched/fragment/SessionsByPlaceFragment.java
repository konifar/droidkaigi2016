package io.github.droidkaigi.confsched.fragment;

import java.util.List;

import io.github.droidkaigi.confsched.model.Session;
import rx.Observable;
import rx.Subscription;

public class SessionsByPlaceFragment extends SessionsFragment {

    public static SessionsByPlaceFragment newInstance() {
        return new SessionsByPlaceFragment();
    }

    @Override
    protected Subscription loadData() {
        Observable<List<Session>> cachedSessions = dao.findByChecked();
        return cachedSessions.subscribe(sessions -> {
            if (sessions.isEmpty()) {
                showEmptyView();
            } else {
                groupByDateSessions(sessions);
            }
        });
    }

}
