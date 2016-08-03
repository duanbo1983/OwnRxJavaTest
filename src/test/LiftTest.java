package test;

import rx.Observable;
import rx.Subscriber;

public class LiftTest {

    public static void main(String[] strings) {
        Observable.just(1).lift(new Observable.Operator<String, Integer>() {
            @Override
            public Subscriber<? super Integer> call(final Subscriber<? super String> subscriber) {
                return new Subscriber<Integer>() {
                    @Override
                    public void onNext(Integer integer) {
                        subscriber.onNext("integer is: " + integer);
                    }

                    @Override
                    public void onCompleted() {
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        subscriber.onError(e);
                    }
                };
            }
        }).subscribe(System.out::println);
    }
}
