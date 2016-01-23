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
import com.konifar.confsched.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static SessionsFragment create() {
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
        Subscription sub = client.getSessions()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sessions -> {
                            dao.deleteAll();
                            dao.insertAll(sessions);
                            groupByDateSessions(sessions);

                        },
                        throwable -> Log.e(TAG, throwable.getMessage(), throwable)
                );
        compositeSubscription.add(sub);
    }

    private void groupByDateSessions(List<Session> sessions) {
        Observable.from(sessions)
                .groupBy(session -> session.stime)
                .subscribe(grouped -> {
                    grouped.toList().subscribe(list -> addFragment(grouped.getKey(), list));
                });
    }

    private void addFragment(Date date, List<Session> sessions) {
        String dateString = DateUtil.getMonthDate(date, getActivity());
        SessionsTabFragment fragment = SessionsTabFragment.newInstance(dateString, sessions);
        Log.e(TAG, "sessions " + dateString + ": " + sessions.size());
        adapter.add(fragment);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText(fragment.getDate()));
    }

    private class SessionsPagerAdapter extends FragmentPagerAdapter {

        private List<SessionsTabFragment> fragments;

        public SessionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new ArrayList<>();
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
            return fragments.get(position).getDate();
        }

        public void add(SessionsTabFragment fragment) {
            fragments.add(fragment);
            notifyDataSetChanged();
        }

    }

}
