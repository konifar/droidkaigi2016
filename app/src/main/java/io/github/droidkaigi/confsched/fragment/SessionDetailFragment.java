package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSessionDetailBinding;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AlarmUtil;
import io.github.droidkaigi.confsched.util.AppUtil;
import io.github.droidkaigi.confsched.util.IntentUtil;

public class SessionDetailFragment extends BaseFragment {

    @Inject
    SessionDao dao;
    @Inject
    ActivityNavigator activityNavigator;

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
        Activity activity = getActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change theme by category
            activity.setTheme(session.category.getThemeResId());
        }
        AppUtil.setTaskDescription(activity, session.title, ContextCompat.getColor(activity, session.category.getVividColorResId()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        initToolbar();
        initLayout();
        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getComponent().inject(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (session != null && !TextUtils.isEmpty(session.shareUrl)) {
            menuInflater.inflate(R.menu.menu_session_detail, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                if (!TextUtils.isEmpty(session.shareUrl)) {
                    IntentUtil.share(getContext(), session.shareUrl);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        AppCompatActivity activity = ((AppCompatActivity) getActivity());
        activity.setSupportActionBar(binding.toolbar);
        ActionBar bar = activity.getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }
    }

    private void initLayout() {
        binding.setSession(session);
        binding.fab.setOnClickListener(v -> {
            boolean checked = !binding.fab.isSelected();
            binding.fab.setSelected(checked);
            session.checked = checked;
            dao.updateChecked(session);
            setResult();
            AlarmUtil.handleSessionAlarm(getActivity(), session);
        });
        binding.txtFeedback.setOnClickListener(v -> activityNavigator.showFeedback(getActivity(), session));
        binding.iconSlide.setOnClickListener(this::onClickIconSlide);
        binding.iconMovie.setOnClickListener(this::onClickIconMovie);
    }

    private void onClickIconSlide(View view) {
        if (session.hasSlide()) {
            IntentUtil.toBrowser(getContext(), session.slideUrl);
        }
    }

    private void onClickIconMovie(View view) {
        if (session.hasDashVideo()) {
            // launch movie screen.
        }
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        getActivity().setResult(Activity.RESULT_OK, intent);
    }

}
