package io.github.droidkaigi.confsched.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import java.util.List;

import io.github.droidkaigi.confsched.model.Page;
import io.github.droidkaigi.confsched.model.Session;
import rx.Observable;
import rx.Subscription;

public class MyScheduleFragment extends SessionsFragment {

    public static MyScheduleFragment newInstance() {
        return new MyScheduleFragment();
    }

    protected void initTabLayout(TabLayout tabLayout) {
        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), Page.MY_SCHEDULE.getToolbarColor()));
    }

    @Override
    protected Subscription loadData() {
        showLoadingView();
        Observable<List<Session>> cachedSessions = dao.findByChecked();
        return cachedSessions.subscribe(sessions -> {
            hideLoadingView();
            if (sessions.isEmpty()) {
                showEmptyView();
            } else {
                groupByDateSessions(sessions);
            }
        });
    }

    @Override
    protected SessionsTabFragment createTabFragment(List<Session> sessions) {
        return MyScheduleSessionsTabFragment.newInstance(sessions);
    }
}
