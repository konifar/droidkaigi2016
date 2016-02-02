package io.github.droidkaigi.confsched.fragment;

import io.github.droidkaigi.confsched.model.Session;

import java.util.List;

import rx.Observable;

public class MyScheduleFragment extends SessionsFragment {

    public static MyScheduleFragment newInstance() {
        return new MyScheduleFragment();
    }

    @Override
    protected void loadData() {
        Observable<List<Session>> cachedSessions = dao.findByChecked();
        if (cachedSessions.isEmpty().toBlocking().single()) {
            // TODO
        } else {
            groupByDateSessions(cachedSessions.toBlocking().single());
        }
    }

}
