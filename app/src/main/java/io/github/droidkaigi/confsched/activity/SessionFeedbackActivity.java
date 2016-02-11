package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.databinding.ActivitySessionFeedbackBinding;
import io.github.droidkaigi.confsched.model.Session;

public class SessionFeedbackActivity extends AppCompatActivity {
    private ActivitySessionFeedbackBinding binding;
    private Session session;

    private static Intent createIntent(@NonNull Context context, @NonNull Session session) {
        Intent intent = new Intent(context, SessionFeedbackActivity.class);
        intent.putExtra(Session.class.getSimpleName(), Parcels.wrap(session));
        return intent;
    }

    public static void start(Context context, Session session) {
        Intent intent = createIntent(context, session);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_session_feedback);

        session = Parcels.unwrap(getIntent().getParcelableExtra(Session.class.getSimpleName()));

        initToolbar(session.title);

        binding.submitFeedbackButton.setOnClickListener(this::onSubmitFeedbackButton);
    }

    private void onSubmitFeedbackButton(View view) {
        // TODO submit the result
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(String title) {
        setSupportActionBar(binding.toolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setDisplayShowHomeEnabled(true);
            bar.setDisplayShowTitleEnabled(false);
            bar.setHomeButtonEnabled(true);
        }

        binding.toolbar.setTitle(title);
    }
}
