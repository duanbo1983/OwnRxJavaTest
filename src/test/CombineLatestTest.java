package test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;

public class CombineLatestTest {

    public static void main(String[] strings) {

        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create("012");
        BehaviorSubject<String> publishSubject = BehaviorSubject.create();

        Observable<String> observable = Observable.combineLatest(behaviorSubject.asObservable(), publishSubject
                .asObservable(),
                new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        return s + ", " + s2;
                    }
                });

        publishSubject.onNext("abc");

        publishSubject.onNext("cde");

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        //publishSubject.onNext("efg");
    }
}
