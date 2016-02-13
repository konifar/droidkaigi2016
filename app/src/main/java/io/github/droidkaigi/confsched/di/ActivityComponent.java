package io.github.droidkaigi.confsched.di;

import dagger.Subcomponent;
import io.github.droidkaigi.confsched.activity.MainActivity;
import io.github.droidkaigi.confsched.activity.SearchActivity;
import io.github.droidkaigi.confsched.activity.SearchedSessionsActivity;
import io.github.droidkaigi.confsched.activity.SessionDetailActivity;
import io.github.droidkaigi.confsched.activity.SessionFeedbackActivity;
import io.github.droidkaigi.confsched.di.scope.ActivityScope;

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);

    void inject(SessionDetailActivity activity);

    void inject(SearchActivity activity);

    void inject(SearchedSessionsActivity activity);

    void inject(SessionFeedbackActivity activity);

    FragmentComponent plus(FragmentModule module);

}
