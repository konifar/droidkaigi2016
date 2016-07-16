package io.github.droidkaigi.confsched;

import android.app.Application;
import android.support.annotation.NonNull;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import io.github.droidkaigi.StethoWrapper;
import io.github.droidkaigi.confsched.di.AppComponent;
import io.github.droidkaigi.confsched.di.AppModule;
import io.github.droidkaigi.confsched.di.DaggerAppComponent;
import io.github.droidkaigi.confsched.util.LocaleUtil;

public class MainApplication extends Application {

    AppComponent appComponent;

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

        LocaleUtil.initLocale(this);
    }

}
