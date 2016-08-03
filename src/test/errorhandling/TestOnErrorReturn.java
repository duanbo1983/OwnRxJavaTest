package test.errorhandling;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class TestOnErrorReturn {

    public static void main(String strings[]) {

        Observable.<String>error(new RuntimeException())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("doOnError: " + throwable);
                    }
                }).onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return "Error";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
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
