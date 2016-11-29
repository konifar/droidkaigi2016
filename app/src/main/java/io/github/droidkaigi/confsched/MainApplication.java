package io.github.droidkaigi.confsched;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import dagger.android.AndroidMemorySensitiveReferenceManager;
import io.fabric.sdk.android.Fabric;
import io.github.droidkaigi.StethoWrapper;
import io.github.droidkaigi.confsched.di.AppComponent;
import io.github.droidkaigi.confsched.di.AppModule;
import io.github.droidkaigi.confsched.di.DaggerAppComponent;
import io.github.droidkaigi.confsched.util.LocaleUtil;

public class MainApplication extends Application {

    AppComponent appComponent;

    @Inject
    AndroidMemorySensitiveReferenceManager manager;

    @NonNull
    public AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        Fabric.with(this, new Crashlytics());

        new StethoWrapper(this).setup();

        AndroidThreeTen.init(this);

        LocaleUtil.initLocale(this);
    }

    @Override
    public void onTrimMemory(int level) {
        manager.onTrimMemory(level);
    }
}
