package io.github.droidkaigi.confsched.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.github.droidkaigi.confsched.MainApplication;
import io.github.droidkaigi.confsched.di.ActivityComponent;
import io.github.droidkaigi.confsched.di.ActivityModule;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @NonNull
    public ActivityComponent getComponent() {
        if (activityComponent == null) {
            MainApplication mainApplication = (MainApplication) getApplication();
            activityComponent = mainApplication.getComponent().plus(new ActivityModule(this));
        }
        return activityComponent;
    }
}
