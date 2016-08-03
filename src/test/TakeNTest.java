package test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

public class TakeNTest {

    public static void main(String[] strings) {

        Observable<Long> longOb = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                for (int i = 0; i < 5 ; i ++) {
                    subscriber.onNext((long) i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onCompleted();
            }
        });

        longOb
                .takeLast(2)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println(aLong);
                    }
                });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
