package test;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

public class ShareTest {

    public static void main(String[] strings) {
        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("1");
        Observable<String> stringObservable = behaviorSubject.asObservable().doOnSubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnSubscribe");
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnUnsubscribe");
            }
        }).replay(1).refCount();

        behaviorSubject.onNext("2");
        behaviorSubject.onNext("3");

        Subscription subscription1 = stringObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("subscription1: " + s);
            }
        });

        Subscription subscription0 = stringObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("subscription0: " + s);
            }
        });

        behaviorSubject.onNext("4");

        subscription1.unsubscribe();

        behaviorSubject.onNext("5");

        subscription0.unsubscribe();

        behaviorSubject.onNext("6");
    }
}
