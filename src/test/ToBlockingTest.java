package test;

import rx.Observable;
import rx.functions.Action1;

import java.util.concurrent.TimeUnit;

/**
 * Created by bod on 9/23/16.
 */
public class ToBlockingTest {

    public static void main(String[] strings) {

        final Observable<Long> interval = Observable.interval(5, 2, TimeUnit.SECONDS);
        interval.take(1).toBlocking().subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("toB: " + aLong);
            }
        });
        System.out.println("DB-Test end");
    }
}
