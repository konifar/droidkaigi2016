package com.konifar.confsched;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.konifar.confsched.di.ActivityComponent;
import com.konifar.confsched.di.ActivityModule;
import com.konifar.confsched.di.AppComponent;
import com.konifar.confsched.di.AppModule;
import com.konifar.confsched.di.DaggerAppComponent;
import com.konifar.confsched.di.FragmentComponent;
import com.konifar.confsched.di.FragmentModule;

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

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        AndroidThreeTen.init(this);
    }

}
