package test.rxjava2;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by bod on 1/24/17.
 */
public class CompletableTest {

    public static void main(String[] strings) {

        final Completable test = Single.just(1).doOnSuccess(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Exceptions.propagate(new IOException("Test"));
            }
        }).toObservable().ignoreElements();

        test.subscribe(new Action() {
            @Override
            public void run() throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println(throwable);
            }
        });
    }
}
