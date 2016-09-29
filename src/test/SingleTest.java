package test;

import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by bod on 9/26/16.
 */
public class SingleTest {

    public static void main(String[] strings) {

        BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                behaviorSubject.onNext("abc");
                behaviorSubject.onCompleted();
            }
        }).start();

        System.out.println("wait");
        behaviorSubject.single().toBlocking().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println(s);
            }
        });

        System.out.println("got value");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
