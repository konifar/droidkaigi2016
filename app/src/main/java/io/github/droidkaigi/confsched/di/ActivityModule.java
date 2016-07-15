package io.github.droidkaigi.confsched.di;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import dagger.Module;
import dagger.Provides;
import io.github.droidkaigi.confsched.activity.PageNavigator;

@Module
public class ActivityModule {

    final AppCompatActivity activity;

    public ActivityModule(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Provides
    public AppCompatActivity activity() {
        return activity;
    }

    @Provides
    LayoutInflater layoutInflater() {
        return activity.getLayoutInflater();
    }

    @Provides
    public PageNavigator providePageNavigator(AppCompatActivity activity) {
        return new PageNavigator(activity);
    }

}
