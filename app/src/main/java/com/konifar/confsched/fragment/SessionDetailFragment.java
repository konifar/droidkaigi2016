package com.konifar.confsched.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.dao.SessionDao;
import com.konifar.confsched.databinding.FragmentSessionDetailBinding;
import com.konifar.confsched.model.Session;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.parceler.Parcels;

import javax.inject.Inject;

public class SessionDetailFragment extends Fragment {

    private static final String TAG = SessionDetailFragment.class.getSimpleName();

    @Inject
    SessionDao dao;
    private FragmentSessionDetailBinding binding;
    private Session session;

    public static SessionDetailFragment create(@NonNull Session session) {
        SessionDetailFragment fragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(Session.class.getSimpleName(), Parcels.wrap(session));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = Parcels.unwrap(getArguments().getParcelable(Session.class.getSimpleName()));
    }

    private void initToolbar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) bar.setDisplayHomeAsUpEnabled(true);
        binding.toolbar.setNavigationOnClickListener(v -> activity.finish());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        initToolbar();
        binding.setSession(session);

        binding.btnStar.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                toggle(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                toggle(false);
            }

            private void toggle(boolean checked) {
                session.checked = checked;
                dao.updateChecked(session);
                setResult();
            }
        });

        return binding.getRoot();
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
    }

}
