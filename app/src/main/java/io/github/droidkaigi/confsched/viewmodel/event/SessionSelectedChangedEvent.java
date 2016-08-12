package io.github.droidkaigi.confsched.viewmodel.event;

import io.github.droidkaigi.confsched.model.Session;

public class SessionSelectedChangedEvent {

    public Session session;

    public SessionSelectedChangedEvent(Session session) {
        this.session = session;
    }

}
