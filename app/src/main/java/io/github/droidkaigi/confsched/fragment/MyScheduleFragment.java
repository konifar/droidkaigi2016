package io.github.droidkaigi.confsched.fragment;

import java.util.List;

import io.github.droidkaigi.confsched.model.Session;
import rx.Observable;
import rx.Subscription;

public class MyScheduleFragment extends SessionsFragment {

    public static MyScheduleFragment newInstance() {
        return new MyScheduleFragment();
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
