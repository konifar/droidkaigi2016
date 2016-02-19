package io.github.droidkaigi.confsched.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.R;
import io.github.droidkaigi.confsched.api.DroidKaigiClient;
import io.github.droidkaigi.confsched.databinding.ActivitySessionFeedbackBinding;
import io.github.droidkaigi.confsched.model.Session;
import io.github.droidkaigi.confsched.model.SessionFeedback;
import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SessionFeedbackActivity extends AppCompatActivity {
    private static final String TAG = SessionFeedbackActivity.class.getName();
    private ActivitySessionFeedbackBinding binding;
    private Session session;
    private Subscription subscription;
    @Inject
    DroidKaigiClient client;

    public static Intent createIntent(@NonNull Context context, @NonNull Session session) {
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
        MainApplication.getComponent(this).inject(this);
        session = Parcels.unwrap(getIntent().getParcelableExtra(Session.class.getSimpleName()));

        initToolbar(session.title);

        binding.submitFeedbackButton.setOnClickListener(this::onSubmitFeedbackButton);
    }

    private void onSubmitFeedbackButton(View view) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
        final int relevancy = binding.relevantFeedbackBar.getProgress();
        final int asExpected = binding.asExpectedFeedbackBar.getProgress();
        final int difficulty = binding.difficultyFeedbackBar.getProgress();
        final int knowledgeable = binding.knowledgeableFeedbackBar.getProgress();
        final String comment = binding.otherCommentsFeedbackText.getText().toString().trim();
        final SessionFeedback feedback = new SessionFeedback(session.id, session.title, relevancy, asExpected, difficulty, knowledgeable, comment);
        subscription = client.submitSessionFeedback(feedback)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onSubmitFeedbackSuccess, this::onSubmitFeedbackFailure);
    }

    private void onSubmitFeedbackSuccess(Response<Void> response) {
        if (response.isSuccess()) {
            final List<String> pathSegments = response.raw().request().url().pathSegments();
            if (!pathSegments.isEmpty() && "closedform".equals(pathSegments.get(pathSegments.size() - 1))) {
                showDialog(R.string.session_feedback_submit_failure, R.string.session_feedback_not_accepting, (dialog, which) -> dialog.dismiss());
            } else {
                Log.i(TAG, "Successfully sent a session feedback for [" + session.id + "]");
                showDialog(R.string.session_feedback_submit_success, R.string.session_feedback_accepted_successfully, (dialog, which) -> {
                    dialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                });
            }
        } else {
            showDialog(R.string.session_feedback_submit_failure, R.string.session_feedback_submit_error, (dialog, which) -> dialog.dismiss());
            try {
                final String err = response.errorBody().string();
                final IOException e = new IOException("Failed to POST feedback: " + response.code());
                Log.e(TAG, "Failed to send the session feedback: [" + err + "]", e);
            } catch (IOException e) {
                Log.e(TAG, "Failed to parse the error [" + response.code() + "]: " + e.getMessage(), e);
            }
        }
    }

    private void showDialog(int titleId, int messageId, DialogInterface.OnClickListener okCallback) {
        new AlertDialog.Builder(this)
                .setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(R.string.ok, okCallback)
                .show();
    }

    private void onSubmitFeedbackFailure(Throwable throwable) {
        Toast.makeText(SessionFeedbackActivity.this, R.string.session_feedback_submit_error, Toast.LENGTH_LONG).show();
        Log.e(TAG, "Failed to send the session feedback: " + throwable.getMessage(), throwable);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
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
