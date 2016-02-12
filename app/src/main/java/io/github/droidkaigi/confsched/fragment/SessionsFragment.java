package io.github.droidkaigi.confsched.fragment;

import org.parceler.Parcels;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.activity.SearchActivity;
import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSessionsBinding;
import io.github.droidkaigi.confsched.model.MainContentStateBrokerProvider;
import io.github.droidkaigi.confsched.model.Page;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.util.DateUtil;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SessionsFragment extends Fragment {

    public interface OnChangeSessionListener {
        void onChangeSession(List<Session> sessions);
    }

    public static final String TAG = SessionsFragment.class.getSimpleName();
    private static final String ARG_SHOULD_REFRESH = "should_refresh";

    private static final int REQ_SEARCH = 2;

    @Inject
    DroidKaigiClient client;
    @Inject
    SessionDao dao;
    @Inject
    CompositeSubscription compositeSubscription;
    @Inject
    ActivityNavigator activityNavigator;
    @Inject
    MainContentStateBrokerProvider brokerProvider;

    private SessionsPagerAdapter adapter;
    private FragmentSessionsBinding binding;
    private boolean shouldRefresh;

    private OnChangeSessionListener onChangeSessionListener = session -> { /*no op*/ };

    public static SessionsFragment newInstance() {
        return newInstance(false);
    }

    public static SessionsFragment newInstance(boolean shouldRefresh) {
        SessionsFragment fragment = new SessionsFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOULD_REFRESH, shouldRefresh);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionsBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        initEmptyView();
        compositeSubscription.add(loadData());
        compositeSubscription.add(fetchAndSave());
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.shouldRefresh = getArguments().getBoolean(ARG_SHOULD_REFRESH, false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
        if (context instanceof OnChangeSessionListener) {
            onChangeSessionListener = (OnChangeSessionListener) context;
        }
    }

    private void initEmptyView() {
        binding.emptyViewButton.setOnClickListener(v -> {
            brokerProvider.get().set(Page.ALL_SESSIONS);
        });
    }

    private Subscription fetchAndSave() {
        return client.getSessions(AppUtil.getCurrentLanguageId(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        dao::updateAllAsync,
                        throwable -> Log.e(TAG, "Failed to fetchAndSave.", throwable)
                );
    }

    protected Subscription loadData() {
        Observable<List<Session>> cachedSessions = dao.findAll();
        return cachedSessions.flatMap(sessions -> {
            if (shouldRefresh || sessions.isEmpty()) {
                return client.getSessions(AppUtil.getCurrentLanguageId(getActivity()))
                        .doOnNext(dao::updateAllAsync);
            } else {
                return Observable.just(sessions);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onLoadDataSuccess,
                        this::onLoadDataFailure
                );
    }

    private void onLoadDataSuccess(List<Session> sessions) {
        Log.i(TAG, "Sessions Load succeeded.");
        groupByDateSessions(sessions);
    }

    private void onLoadDataFailure(Throwable throwable) {
        Log.e(TAG, "Sessions Load failed", throwable);
        Snackbar.make(binding.containerMain, R.string.sessions_network_error, Snackbar.LENGTH_LONG).show();
    }

    protected void showEmptyView() {
        binding.emptyView.setVisibility(View.VISIBLE);
    }

    protected void hideEmptyView() {
        binding.emptyView.setVisibility(View.GONE);
    }

    protected void groupByDateSessions(List<Session> sessions) {
        Map<String, List<Session>> sessionsByDate = new TreeMap<>();
        for (Session session : sessions) {
            String key = DateUtil.getMonthDate(session.stime, getActivity());
            if (sessionsByDate.containsKey(key)) {
                sessionsByDate.get(key).add(session);
            } else {
                List<Session> list = new ArrayList<>();
                list.add(session);
                sessionsByDate.put(key, list);
            }
        }

        adapter = new SessionsPagerAdapter(getFragmentManager());

        for (Map.Entry<String, List<Session>> e : sessionsByDate.entrySet()) {
            addFragment(e.getKey(), e.getValue());
        }

        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        if (sessions.isEmpty()) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
    }

    private void addFragment(String title, List<Session> sessions) {
        SessionsTabFragment fragment = SessionsTabFragment.newInstance(sessions);
        adapter.add(title, fragment);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_sessions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_search:
                activityNavigator.showSearch(this, REQ_SEARCH);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQ_SEARCH) {
            return;
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        List<Session> statusChangedSession = Parcels
                .unwrap(data.getParcelableExtra(SearchActivity.RESULT_STATUS_CHANGED_SESSIONS));
        if (statusChangedSession == null || statusChangedSession.isEmpty()) {
            return;
        }
        onChangeSessionListener.onChangeSession(statusChangedSession);
        compositeSubscription.add(loadData());
    }

    private class SessionsPagerAdapter extends FragmentStatePagerAdapter {

        private List<SessionsTabFragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public SessionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        @Nullable
        public Fragment getItem(int position) {
            if (position >= 0 && position < fragments.size()) {
                return fragments.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public void add(String title, SessionsTabFragment fragment) {
            fragments.add(fragment);
            titles.add(title);
            notifyDataSetChanged();
        }

    }

}
