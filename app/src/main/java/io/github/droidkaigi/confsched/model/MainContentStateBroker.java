package io.github.droidkaigi.confsched.model;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * @author KeishinYokomaku
 */
public class MainContentStateBroker {
    private final Subject<Page, Page> bus = new SerializedSubject<>(PublishSubject.create());

    public void set(Page page) {
        bus.onNext(page);
    }

    public Observable<Page> observe() {
        return bus;
    }
}
