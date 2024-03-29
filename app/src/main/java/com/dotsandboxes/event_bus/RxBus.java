package com.dotsandboxes.event_bus;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import timber.log.Timber;


public class RxBus {

    private static RxBus instance;

    private final Subject<Object, Object> bus = new SerializedSubject<>(PublishSubject.create());

    private RxBus() {

    }

    public static RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public void send(Object o) {
        try {
            bus.onNext(o);
        } catch (Exception e) {
            Timber.e(e.getCause());
        }

    }

    public Observable<Object> getBus() {
        return bus;
    }

    public Subscription subscribe(final Class filter, Subscriber<? super Object> subscriber) {
        return bus.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return filter == null || filter.isInstance(o);
            }
        }).subscribe(subscriber);
    }


    public void unSubscribe(Subscription subscription) {
        if (subscription != null && subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}

