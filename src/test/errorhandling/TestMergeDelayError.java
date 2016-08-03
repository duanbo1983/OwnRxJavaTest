package test.errorhandling;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class TestMergeDelayError {

    public static Observable<String> getUserIds() {
        return Observable.from(new String[] {"1", "2", "3", "4", "5", "6"});
    }

    public static Observable<String> getInfo(final String prefix, final String integer, final String errorNumber) {
        return Observable.create(new Observable.OnSubscribe<String>() {
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
    }

    public static void main(String[] args) {
        Observable<String> userIdObservable = getUserIds();
        Observable<String> t = userIdObservable.flatMap(new Func1<String, Observable<String>>() {

            public Observable<String> call(final String t1) {
                Observable<String> info1 = getInfo("1::: ", t1, "2");
                Observable<String> info2 = getInfo("2::: ", t1, "3");
                return Observable.mergeDelayError(info1, info2);
            }
        });

        t.subscribe(new Action1<String>() {
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
}