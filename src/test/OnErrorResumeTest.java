package test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by bod on 12/7/16.
 */
public class OnErrorResumeTest {

    public static void main(String[] strings) {

        Observable.error(new RuntimeException("aa"))
                .onErrorResumeNext(new Func1<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> call(Throwable throwable) {
                        return Observable.error(new Exception("dd"));
                    }
                })
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        System.out.println();
                    }
                });

    }
}
