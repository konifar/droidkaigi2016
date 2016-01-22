package com.konifar.confsched.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.konifar.confsched.R;
import com.konifar.confsched.dao.SessionDao;
import com.konifar.confsched.databinding.FragmentSessionsBinding;
import com.konifar.confsched.databinding.ItemSessionBinding;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.widget.ArrayRecyclerAdapter;
import com.konifar.confsched.widget.BindingHolder;
import com.konifar.confsched.widget.OnItemClickListener;

import javax.inject.Inject;

public class SessionsFragment extends Fragment implements OnItemClickListener<Session> {

    private static String TAG = SessionsFragment.class.getSimpleName();

    @Inject
    SessionDao dao;

    private SessionsAdapter adapter;
    private FragmentSessionsBinding binding;

    public static SessionsFragment create() {
        return new SessionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionsBinding.inflate(inflater, container, false);
        bindData();
        return binding.getRoot();
    }

    private void bindData() {
        adapter = new SessionsAdapter(getActivity());
        adapter.setOnItemClickListener(this);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(@NonNull View view, Session item) {
        // TODO
    }

    private class SessionsAdapter extends ArrayRecyclerAdapter<Session, BindingHolder<ItemSessionBinding>> {

        public SessionsAdapter(@NonNull Context context) {
            super(context);
        }

        @Override
        public BindingHolder<ItemSessionBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BindingHolder<>(getContext(), parent, R.layout.item_session);
        }

        @Override
        public void onBindViewHolder(BindingHolder<ItemSessionBinding> holder, int position) {

        }

    }

}
