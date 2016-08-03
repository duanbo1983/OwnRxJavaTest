package test.backpressure;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RequestMultiple {

    public static void main(String[] strings) {

        Integer[] integers = new Integer[1000];
        for (int i = 0; i < 1000; i ++) {
            integers[i] = i;
        }

        Observable<Integer> integerObservable = Observable.from(integers);

        Observable<Integer> slowObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {
                Schedulers.newThread().createWorker().schedulePeriodically(new Action0() {
                    @Override
                    public void call() {
                        subscriber.onNext(10000);
                    }
                }, 0, 1000, TimeUnit.MILLISECONDS);
            }
        });

        Observable zip = Observable.zip(integerObservable, slowObservable, new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer o, Integer o2) {
                System.out.println("o1: " + o + ", o2: " + o2);
                return o + o2;
            }
        });

        Subscription subscription = zip.subscribe(new Action1() {
            @Override
            public void call(Object o) {
                System.out.println("o3: " + o);
            }
        });

        synchronized (subscription) {
            try {
                subscription.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
