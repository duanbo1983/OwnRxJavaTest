package test;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class TakeLast {

    public static void main(String[] strings) {

        Observable<String> observable = Observable.empty();
        observable.takeLast(1).observeOn(Schedulers.newThread()).doOnNext(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end");
    }
}
