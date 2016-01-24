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

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.R;
import com.konifar.confsched.dao.SessionDao;
import com.konifar.confsched.databinding.FragmentSessionsTabBinding;
import com.konifar.confsched.databinding.ItemSessionBinding;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.widget.ArrayRecyclerAdapter;
import com.konifar.confsched.widget.BindingHolder;
import com.konifar.confsched.widget.OnItemClickListener;
import com.konifar.confsched.widget.SpaceItemDecoration;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

public class SessionsTabFragment extends Fragment implements OnItemClickListener<Session> {

    private static final String TAG = SessionsTabFragment.class.getSimpleName();
    private static final String ARG_SESSIONS = "sessions";

    @Inject
    SessionDao dao;

    private SessionsAdapter adapter;
    private FragmentSessionsTabBinding binding;

    private List<Session> sessions;

    @NonNull
    public static SessionsTabFragment newInstance(List<Session> sessions) {
        SessionsTabFragment fragment = new SessionsTabFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_SESSIONS, Parcels.wrap(sessions));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessions = Parcels.unwrap(getArguments().getParcelable(ARG_SESSIONS));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainApplication.getComponent(this).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionsTabBinding.inflate(inflater, container, false);
        bindData();
        return binding.getRoot();
    }

    private void bindData() {
        adapter = new SessionsAdapter(getActivity());
        adapter.setOnItemClickListener(this);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_xsmall);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
        adapter.addAll(sessions);
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
            Session session = getItem(position);
            ItemSessionBinding binding = holder.binding;
            binding.setSession(session);

            if (position > 0 && position < getItemCount()) {
                Session prevSession = getItem(position - 1);
                if (prevSession.stime.getTime() == session.stime.getTime()) {
                    binding.txtStime.setVisibility(View.INVISIBLE);
                } else {
                    binding.txtStime.setVisibility(View.VISIBLE);
                }
            } else {
                binding.txtStime.setVisibility(View.VISIBLE);

            }

            binding.btnStar.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    session.checked = true;
                    dao.updateChecked(session);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    session.checked = false;
                    dao.updateChecked(session);

                }
            });
        }

    }

}
