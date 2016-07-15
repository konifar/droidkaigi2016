package io.github.droidkaigi.confsched.viewmodel.event;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

public class EventBus {

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
