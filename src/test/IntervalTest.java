package test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class IntervalTest {

    static String lastStr = "";

    public static void main(String[] strings) {

        Observable<String> intervalObservable = Observable.interval(1, 1, TimeUnit.SECONDS).map(new Func1<Long, String>
                () {
            @Override
            public String call(Long aLong) {
                return lastStr;
            }
        });

        Observable<String> originalObservable = Observable.just("0", "1", "2", "3", "4", "5");
        originalObservable = originalObservable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("DB-Test its started");
            }
        });

        Observable<String> mergedObservable = Observable.merge(intervalObservable, originalObservable);

        mergedObservable.observeOn(Schedulers.computation()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                lastStr = s;
                System.out.println("DB-Test --> str: " + s);
            }
        });

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
