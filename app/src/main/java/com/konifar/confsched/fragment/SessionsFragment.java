package com.konifar.confsched.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.api.DroidKaigiClient;
import com.konifar.confsched.dao.SessionDao;
import com.konifar.confsched.databinding.FragmentSessionsBinding;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.util.AppUtil;
import com.konifar.confsched.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class SessionsFragment extends Fragment {

    private static final String TAG = SessionsFragment.class.getSimpleName();

    @Inject
    DroidKaigiClient client;
    @Inject
    SessionDao dao;
    @Inject
    CompositeSubscription compositeSubscription;

    private SessionsPagerAdapter adapter;
    private FragmentSessionsBinding binding;

    public static SessionsFragment newInstance() {
        return new SessionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionsBinding.inflate(inflater, container, false);
        initViewPager();
        loadData();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
    }

    private void initViewPager() {
        adapter = new SessionsPagerAdapter(getFragmentManager());
        binding.viewPager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void loadData() {
        Observable<List<Session>> cachedSessions = dao.findAll();
        if (cachedSessions.isEmpty().toBlocking().single()) {
            Subscription sub = client.getSessions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(sessions -> {
                                dao.updateAll(sessions);
                                groupByDateSessions(sessions);
                            },
                            throwable -> Log.e(TAG, throwable.getMessage(), throwable)
                    );
            compositeSubscription.add(sub);
        } else {
            groupByDateSessions(cachedSessions.toBlocking().single());
        }
    }

    private void groupByDateSessions(List<Session> sessions) {
        Map<String, List<Session>> sessionsByDate = new TreeMap<>();
        for (Session session : sessions) {
            String key = DateUtil.getMonthDate(session.stime, AppUtil.getLocale(), getActivity());
            if (sessionsByDate.containsKey(key)) {
                sessionsByDate.get(key).add(session);
            } else {
                List<Session> list = new ArrayList<>();
                list.add(session);
                sessionsByDate.put(key, list);
            }
        }

        for (Map.Entry<String, List<Session>> e : sessionsByDate.entrySet()) {
            addFragment(e.getKey(), e.getValue());
        }

        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    private void addFragment(String title, List<Session> sessions) {
        SessionsTabFragment fragment = SessionsTabFragment.newInstance(sessions);
        Log.e(TAG, "sessions " + title + ": " + sessions.size());
        adapter.add(title, fragment);
    }

    private class SessionsPagerAdapter extends FragmentPagerAdapter {

        private final List<SessionsTabFragment> fragments = new ArrayList<>();
        private final List<String> titles = new ArrayList<>();

        public SessionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
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
