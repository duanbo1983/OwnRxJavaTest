package test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

public class FireWithDelay {

    public static void main(String[] strings) {
        final long curr = System.currentTimeMillis();
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .map(aLong -> aLong % 2 == 0)
                .subscribeOn(Schedulers.newThread())
                .switchMap(aBoolean -> {
                    if (aBoolean) {
                        return Observable.just(true);
                    } else {
                        return Observable.just(false).delay(700, TimeUnit.MILLISECONDS);
                    }
                }).subscribe(aBoolean -> {
                    System.out.println("time: " + (System.currentTimeMillis() - curr) + ": " + aBoolean);
                });

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
