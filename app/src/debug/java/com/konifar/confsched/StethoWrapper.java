package com.konifar.confsched;

import android.content.Context;

import com.facebook.stetho.Stetho;

import javax.inject.Inject;

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

