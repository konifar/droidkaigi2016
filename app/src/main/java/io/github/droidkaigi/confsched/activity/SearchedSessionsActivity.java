package io.github.droidkaigi.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.dao.SessionDao;
import io.github.droidkaigi.confsched.databinding.ActivitySearchedSessionsBinding;
import io.github.droidkaigi.confsched.fragment.SearchedSessionsFragment;
import io.github.droidkaigi.confsched.fragment.SessionsFragment;
import io.github.droidkaigi.confsched.model.SearchGroup;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.util.AnalyticsTracker;
import io.github.droidkaigi.confsched.util.AppUtil;

public class SearchedSessionsActivity extends AppCompatActivity implements SessionsFragment.OnChangeSessionListener {

    @Inject
    AnalyticsTracker analyticsTracker;
    @Inject
    SessionDao dao;

    private ActivitySearchedSessionsBinding binding;

    private List<Session> statusChangedSession =new ArrayList<>();

    public static Intent createIntent(@NonNull Context context, @NonNull SearchGroup searchGroup) {
        Intent intent = new Intent(context, SearchedSessionsActivity.class);
        intent.putExtra(SearchGroup.class.getSimpleName(), Parcels.wrap(searchGroup));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_searched_sessions);
        MainApplication.getComponent(this).inject(this);

        SearchGroup searchGroup = Parcels.unwrap(getIntent().getParcelableExtra(SearchGroup.class.getSimpleName()));

        initToolbar(searchGroup);
        AppUtil.setTaskDescription(this, searchGroup.getName(), AppUtil.getThemeColorPrimary(this));

        if(savedInstanceState == null) {
            replaceFragment(SearchedSessionsFragment.newInstance(searchGroup));
        }
    }

    private void initToolbar(SearchGroup searchGroup) {
        setSupportActionBar(binding.toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }

        binding.toolbar.setTitle(searchGroup.getName());
    }

    private void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_view, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsTracker.sendScreenView("searchedSessions");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChangeSession(List<Session> sessions) {
        statusChangedSession.addAll(sessions);
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(SearchActivity.RESULT_STATUS_CHANGED_SESSIONS, Parcels.wrap(statusChangedSession));
        setResult(Activity.RESULT_OK, intent);
        super.finish();
    }
}
