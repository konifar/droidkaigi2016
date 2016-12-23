package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.like.LikeButton;
import com.like.OnLikeListener;

import org.parceler.Parcels;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSessionsTabBinding;
import io.github.droidkaigi.confsched.databinding.ItemSessionBinding;
import io.github.droidkaigi.confsched.di.scope.InScope;
import io.github.droidkaigi.confsched.di.scope.ReleaseWhenUiHidden;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AlarmUtil;
import io.github.droidkaigi.confsched.widget.ArrayRecyclerAdapter;
import io.github.droidkaigi.confsched.widget.BindingHolder;
import io.github.droidkaigi.confsched.widget.itemdecoration.SpaceItemDecoration;

public class SessionsTabFragment extends BaseFragment {

    protected static final String ARG_SESSIONS = "sessions";
    private static final int REQ_DETAIL = 1;

    @Inject
    SessionDao dao;
    @InScope(ReleaseWhenUiHidden.class)
    ActivityNavigator activityNavigator;

    private SessionsAdapter adapter;
    private FragmentSessionsTabBinding binding;

    private List<Session> sessions;

    private SessionsFragment.OnChangeSessionListener onChangeSessionListener = sessions -> {/* no op */};

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
        getComponent().inject(this);
        if (context instanceof SessionsFragment.OnChangeSessionListener) {
            onChangeSessionListener = (SessionsFragment.OnChangeSessionListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionsTabBinding.inflate(inflater, container, false);
        bindData();
        return binding.getRoot();
    }

    private void bindData() {
        adapter = createAdapter();

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int spacing = getResources().getDimensionPixelSize(R.dimen.spacing_xsmall);
        binding.recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));
        adapter.addAll(sessions);
    }

    protected SessionsAdapter createAdapter() {
        return new SessionsAdapter(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_DETAIL:
                if (resultCode == Activity.RESULT_OK) {
                    Session session = Parcels.unwrap(data.getParcelableExtra(Session.class.getSimpleName()));
                    if (session != null) {
                        adapter.refresh(session);
                        onChangeSessionListener.onChangeSession(Collections.singletonList(session));
                    }
                }
                break;
        }
    }

    public void scrollUpToTop() {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    protected class SessionsAdapter extends ArrayRecyclerAdapter<Session, BindingHolder<ItemSessionBinding>> {

        public SessionsAdapter(@NonNull Context context) {
            super(context);
        }

        protected void refresh(@NonNull Session session) {
            // TODO It may be heavy logic...
            for (int i = 0, count = adapter.getItemCount(); i < count; i++) {
                Session s = adapter.getItem(i);
                if (session.equals(s)) {
                    s.checked = session.checked;
                    adapter.notifyItemChanged(i);
                }
            }
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

            binding.txtConflict.setVisibility(View.GONE);

            binding.btnStar.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    session.checked = true;
                    final int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        onLikeChanged(session, pos);
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    session.checked = false;
                    final int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        onLikeChanged(session, pos);
                    }
                }
            });

            binding.cardView.setOnClickListener(v ->
                    activityNavigator.showSessionDetail(SessionsTabFragment.this, session, REQ_DETAIL));
        }

        protected void onLikeChanged(@NonNull Session session, int position) {
            dao.updateChecked(session);
            AlarmUtil.handleSessionAlarm(getContext(), session);
            onChangeSessionListener.onChangeSession(Collections.singletonList(session));
        }
    }

}
