package io.github.droidkaigi;

import android.content.Context;

import com.facebook.stetho.Stetho;

import javax.inject.Inject;

import io.github.droidkaigi.confsched.MainApplication;

public class StethoWrapper {

    @Inject
    Context context;

    public StethoWrapper(MainApplication app) {
        app.getComponent().inject(this);
    }

    public void setup() {
        Stetho.initializeWithDefaults(context);
    }

}

