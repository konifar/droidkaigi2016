package io.github.droidkaigi.confsched.model;

import javax.inject.Inject;

/**
 * @author KeishinYokomaku
 */
public class MainContentStateBrokerProvider {
    private static final MainContentStateBroker BROKER = new MainContentStateBroker();

    @Inject
    public MainContentStateBrokerProvider() {
    }

    public MainContentStateBroker get() {
        return BROKER;
    }
}
