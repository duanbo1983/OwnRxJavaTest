package test.errorhandling;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class TestMergeDelayErrorResume {

    public static Observable<String> getUserIds() {
        return Observable.from(new String[] {"1", "2", "3", "4", "5", "6"});
    }

    public static Observable<String> getInfo(final String prefix, final String integer, final String errorNumber) {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (integer.equals(errorNumber)) {
                    subscriber.onError(new RuntimeException("Prefix: " + prefix));
                } else {
                    subscriber.onNext(prefix + integer + ", errorNumber: " + errorNumber);
                    subscriber.onCompleted();
                }
            }
        });
        return observable;
    }

    public static void main(String[] args) {

        Observable<String> userIdObservable = getUserIds();
        userIdObservable.subscribe(new Action1<String>() {

            public void call(String t1) {
                Observable<String> info1 = getInfo("1::: ", t1, "2")
                        .doOnError(new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        })
                        .onErrorResumeNext(Observable.<String>empty());
                info1.subscribe(

                        new Action1<String>() {

                            public void call(String t1) {
                                System.out.println(t1);
                            }
                        }, new Action1<Throwable>() {

                            public void call(Throwable t1) {
                                t1.printStackTrace();
                            }
                        },
                        new Action0() {

                            public void call() {
                                System.out.println("onComplete");
                            }

                        });
            }
        });
    }
}
