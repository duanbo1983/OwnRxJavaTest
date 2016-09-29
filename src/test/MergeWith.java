package test;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Created by bod on 9/29/16.
 */
public class MergeWith {

    public static void main(String[] strings) {

        Observable.interval(1, TimeUnit.SECONDS).mergeWith(Observable.error(new RuntimeException()))
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        System.out.println(aLong);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println(throwable);
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        System.out.println("complete");
                    }
                });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }
}
