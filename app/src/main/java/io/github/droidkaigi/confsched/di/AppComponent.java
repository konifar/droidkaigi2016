package io.github.droidkaigi.confsched.di;

import dagger.android.AndroidMemorySensitiveReferenceManager;
import io.github.droidkaigi.StethoWrapper;

import javax.inject.Singleton;

import dagger.Component;
import io.github.droidkaigi.confsched.di.scope.ReleaseWhenUiHidden;

@Singleton
@ReleaseWhenUiHidden
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(StethoWrapper stethoDelegator);

    ActivityComponent plus(ActivityModule module);

    AndroidMemorySensitiveReferenceManager manager();
}
