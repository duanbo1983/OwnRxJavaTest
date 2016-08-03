package test.errorhandling;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class TestOnErrorResumeNext {

    public static void main(String strings[]) {

        Observable.<Boolean>error(new RuntimeException())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("doOnError: " + throwable);
                    }
                }).onErrorResumeNext(new Func1<Throwable, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Throwable throwable) {
                //return Observable.error(throwable);
                return Observable.just(false);
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean s) {
                System.out.println("onNext: " + s);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("onError: " + throwable);
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("onCompleted");
            }
        });
    }

}
