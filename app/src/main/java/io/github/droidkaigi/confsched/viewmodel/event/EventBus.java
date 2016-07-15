package io.github.droidkaigi.confsched.viewmodel.event;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

@Singleton
public class EventBus {

    @Inject
    public EventBus() {
        //
    }

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    public void post(Object o) {
        bus.onNext(o);
    }

    public <T> Observable<T> observe(Class<T> klass) {
        return observe().ofType(klass);
    }

    public Observable<Object> observe() {
        return bus.asObservable();
    }

}
