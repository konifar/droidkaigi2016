package io.github.droidkaigi.confsched;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

import io.fabric.sdk.android.Fabric;
import io.github.droidkaigi.StethoWrapper;
import io.github.droidkaigi.confsched.di.ActivityComponent;
import io.github.droidkaigi.confsched.di.ActivityModule;
import io.github.droidkaigi.confsched.di.AppComponent;
import io.github.droidkaigi.confsched.di.AppModule;
import io.github.droidkaigi.confsched.di.DaggerAppComponent;
import io.github.droidkaigi.confsched.di.FragmentComponent;
import io.github.droidkaigi.confsched.di.FragmentModule;
import io.github.droidkaigi.confsched.util.LocaleUtil;

public class MainApplication extends Application {

    AppComponent appComponent;

    @NonNull
    public static FragmentComponent getComponent(Fragment fragment) {
        assert fragment.getActivity() != null;
        AppCompatActivity activity = (AppCompatActivity) fragment.getActivity();
        MainApplication application = (MainApplication) fragment.getContext().getApplicationContext();
        return application.appComponent
                .plus(new ActivityModule(activity))
                .plus(new FragmentModule(fragment));
    }

    @NonNull
    public static ActivityComponent getComponent(AppCompatActivity activity) {
        MainApplication application = (MainApplication) activity.getApplicationContext();
        return application.appComponent
                .plus(new ActivityModule(activity));
    }

    @NonNull
    public AppComponent getComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LocaleUtil.initLocale(this);

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        Fabric.with(this, new Crashlytics());

        new StethoWrapper(this).setup();

        AndroidThreeTen.init(this);
    }

}
