package test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by bod on 10/6/16.
 */
public class CreateOrDeferTest {
    static int count = 0;

    public static void main(String[] strings) {

        final Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(count++);
                subscriber.onCompleted();
            }
        });

        integerObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("v: " + integer);
            }
        });

        integerObservable.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("v: " + integer);
            }
        });


    }
}
