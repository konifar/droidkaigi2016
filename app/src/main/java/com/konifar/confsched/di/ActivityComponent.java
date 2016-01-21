package com.konifar.confsched.di;

import com.konifar.confsched.activity.MainActivity;
import com.konifar.confsched.di.scope.ActivityScope;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(MainActivity activity);

    FragmentComponent plus(FragmentModule module);

}
