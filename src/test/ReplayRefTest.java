package test;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.concurrent.Callable;

/**
 * Created by bod on 9/26/16.
 */
public class ReplayRefTest {

    public static void main(String[] strings) {

        final Observable<String> stringObservable = Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println("exec");
                Thread.sleep(1000);
                return "abc";
            }
        }).replay().autoConnect().subscribeOn(Schedulers.io());

        System.out.println("before blocking");
        stringObservable.single().toBlocking().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("first...");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println(throwable);
            }
        });

        System.out.println("---");

        stringObservable.observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("0: " + s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("onComplete");
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        stringObservable.single().toBlocking().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("single blocking");
            }
        });

        System.out.println("2nd sub");
        stringObservable.observeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("1: " + s);
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("end");
    }
}
