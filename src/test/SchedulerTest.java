package test;

import rx.Observable;
import rx.schedulers.Schedulers;

public class SchedulerTest {

    public static void main(String[] strings) {
        Observable.just(1)
                .doOnSubscribe(() -> System.out.println("subscribe from 0: " + Thread.currentThread()))
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(() -> System.out.println("subscribe from 1: " + Thread.currentThread()))
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe(() -> System.out.println("subscribe from 2: " + Thread.currentThread()))
                .observeOn(Schedulers.newThread())
                .doOnNext(integer -> System.out.println("next 0: " + integer + ", from: " + Thread.currentThread()))
                .observeOn(Schedulers.computation())
                .doOnNext(integer -> System.out.println("next 1: " + integer + ", from: " + Thread.currentThread()))
                .observeOn(Schedulers.newThread())
                .subscribe(integer -> {
                    System.out.println(integer + " from: " + Thread.currentThread());
                });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
