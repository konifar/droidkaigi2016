package com.konifar.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ActivitySessionDetailBinding;
import com.konifar.confsched.fragment.SessionDetailFragment;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.util.AnalyticsUtil;

import org.parceler.Parcels;

import javax.inject.Inject;

public class SessionDetailActivity extends AppCompatActivity {

    private static final String TAG = SessionDetailActivity.class.getSimpleName();

    @Inject
    AnalyticsUtil analyticsUtil;

    private ActivitySessionDetailBinding binding;
    private Session session;

    private static Intent createIntent(@NonNull Context context, @NonNull Session session) {
        Intent intent = new Intent(context, SessionDetailActivity.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        return intent;
    }

    static void startForResult(@NonNull Activity activity, @NonNull Session session, int requestCode) {
        Intent intent = createIntent(activity, session);
        activity.startActivityForResult(intent, requestCode);
        activity.overridePendingTransition(R.anim.activity_slide_start_enter, R.anim.activity_scale_start_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_detail);
        MainApplication.getComponent(this).inject(this);

        session = Parcels.unwrap(getIntent().getParcelableExtra(Session.class.getSimpleName()));

        replaceFragment(SessionDetailFragment.create(session));
    }

    private void replaceFragment(Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_view, fragment, fragment.getClass().getSimpleName());
        ft.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsUtil.sendScreenView("session_detail");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_scale_finish_enter, R.anim.activity_slide_finish_exit);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
