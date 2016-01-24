package com.konifar.confsched.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.konifar.confsched.MainApplication;
import com.konifar.confsched.R;
import com.konifar.confsched.databinding.ActivitySessionDetailBinding;
import com.konifar.confsched.model.Session;
import com.konifar.confsched.util.AnalyticsUtil;

import org.parceler.Parcels;

import javax.inject.Inject;

public class SessionDetailActivity extends AppCompatActivity {

    private static final String TAG = SessionDetailActivity.class.getSimpleName();
    private static final String EXTRA_SESSION = "session";

    @Inject
    AnalyticsUtil analyticsUtil;

    private ActivitySessionDetailBinding binding;

    private static Intent createIntent(@NonNull Context context, @NonNull Session session) {
        Intent intent = new Intent(context, SessionDetailActivity.class);
        intent.putExtra(EXTRA_SESSION, Parcels.wrap(session));
        return intent;
    }

    public static void start(@NonNull Activity activity, @NonNull Session session) {
        Intent intent = createIntent(activity, session);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_slide_start_enter, R.anim.activity_scale_start_exit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_detail);
        MainApplication.getComponent(this).inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        analyticsUtil.sendScreenView("session_detail");
    }

}
