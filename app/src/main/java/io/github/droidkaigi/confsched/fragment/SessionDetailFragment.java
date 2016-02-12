package io.github.droidkaigi.confsched.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.activity.ActivityNavigator;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.FragmentSessionDetailBinding;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AppUtil;

public class SessionDetailFragment extends Fragment {

    private static final String TAG = SessionDetailFragment.class.getSimpleName();

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSessionDetailBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        initToolbar();
        binding.setSession(session);

        binding.fab.setOnClickListener(v -> {
            boolean checked = !binding.fab.isSelected();
            binding.fab.setSelected(checked);
            session.checked = checked;
            dao.updateChecked(session);
            setResult();
        });

        binding.txtFeedback.setOnClickListener(v -> activityNavigator.showFeedback(getActivity(), session));

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_session_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_share:
                // TODO share logic
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
