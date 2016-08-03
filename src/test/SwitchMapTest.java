package test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class SwitchMapTest {

    public static void main(String[] strings) {

        Observable.interval(100, TimeUnit.MILLISECONDS)
                .switchMap(i ->
                        Observable.interval(30, TimeUnit.MILLISECONDS)
                                .map(l -> i))
                .take(9)
                .subscribeOn(Schedulers.newThread())
                .subscribe(System.out::println);

//        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for (int i = 0; i < 100; i++) {
//                    subscriber.onNext(i);
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException ignored) {}
//                }
//            }
//        });
//
//        Observable<Integer> switchMap = observable.switchMap(new Func1<Integer, Observable<? extends Integer>>() {
//            @Override
//            public Observable<? extends Integer> call(Integer integer) {
//                return Observable.create(new Observable.OnSubscribe<Integer>() {
//                    @Override
//                    public void call(Subscriber<? super Integer> subscriber) {
//                        try {
//                            Thread.sleep(1000 / (integer + 1));
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        subscriber.onNext(integer * 10);
//                    }
//                });
//            }
//        });
//
//        Subscription subscription0 = switchMap.subscribeOn(Schedulers.newThread()).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer s) {
//                System.out.println("0: " + s);
//            }
//        });
//
//        Subscription subscription1 = switchMap.subscribeOn(Schedulers.newThread()).subscribe(new Action1<Integer>() {
//            @Override
//            public void call(Integer s) {
//                System.out.println("1: " + s);
//            }
//        });
//
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        System.out.println("0 isUnSubscribed? " + subscription0.isUnsubscribed());
//        System.out.println("1 isUnSubscribed? " + subscription1.isUnsubscribed());
        System.out.println("End");
    }
}
