package com.konifar.confsched.di;

import com.konifar.confsched.StethoWrapper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(StethoWrapper stethoDelegator);

    ActivityComponent plus(ActivityModule module);

}
