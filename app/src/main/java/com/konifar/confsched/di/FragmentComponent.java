package com.konifar.confsched.di;

import com.konifar.confsched.di.scope.FragmentScope;
import com.konifar.confsched.fragment.SessionDetailFragment;
import com.konifar.confsched.fragment.SessionsFragment;
import com.konifar.confsched.fragment.SessionsTabFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = {FragmentModule.class})
public interface FragmentComponent {

    void inject(SessionsTabFragment fragment);

    void inject(SessionsFragment fragment);

    void inject(SessionDetailFragment fragment);

}
