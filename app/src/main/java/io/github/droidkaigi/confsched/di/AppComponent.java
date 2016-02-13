package io.github.droidkaigi.confsched.di;

import io.github.droidkaigi.StethoWrapper;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(StethoWrapper stethoDelegator);

    ActivityComponent plus(ActivityModule module);

}
