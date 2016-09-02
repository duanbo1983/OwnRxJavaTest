package test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bod on 9/1/16.
 */
public class TestBufferPair {

    public static void main(String[] strings) {

        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        observable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("raw: " + aLong);
            }
        });

        observable.buffer(2, 1)
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        System.out.println(longs);
                    }
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
