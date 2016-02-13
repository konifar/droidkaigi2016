package io.github.droidkaigi.confsched.di;

import io.github.droidkaigi.confsched.di.scope.FragmentScope;
import io.github.droidkaigi.confsched.fragment.AboutFragment;
import io.github.droidkaigi.confsched.fragment.SessionDetailFragment;
import io.github.droidkaigi.confsched.fragment.SessionsFragment;
import io.github.droidkaigi.confsched.fragment.SessionsTabFragment;
import io.github.droidkaigi.confsched.fragment.SettingsFragment;
import io.github.droidkaigi.confsched.fragment.SponsorsFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(SettingsFragment fragment);

    void inject(AboutFragment fragment);

    void inject(SessionsTabFragment fragment);

    void inject(SessionsFragment fragment);

    void inject(SessionDetailFragment fragment);

    void inject(SponsorsFragment fragment);
}
