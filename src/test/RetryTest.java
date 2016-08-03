package test;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class RetryTest {

    Observable<Result> call() {
        return Observable.create(new Observable.OnSubscribe<Result>() {
            int times = 0;
            @Override
            public void call(Subscriber<? super Result> subscriber) {
                System.out.println("Thread0 --> " + Thread.currentThread());
                System.out.println(System.currentTimeMillis() + ": subscribed");
                synchronized (this) {
                    try {
                        this.wait(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                times ++;
                if (times < 7) {
                    subscriber.onError(new RuntimeException("What"));
                } else {
                    subscriber.onNext(new Result().code(200));
                    subscriber.onCompleted();
                }
            }
        });
    }

    public static void main(String[] strings) {
        final Object obj = new Object();
        RetryTest retryTest = new RetryTest();
        retryTest.call().retryWhen(new RetryWithExponentialDelay(4)).subscribe(new Action1<Result>() {
            @Override
            public void call(Result result) {
                System.out.println("Thread1 --> " + Thread.currentThread());
                System.out.println("Final next: " + result);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("Thread2 --> " + Thread.currentThread());
                System.out.println("Final error: " + throwable);
                synchronized (obj) {
                    obj.notifyAll();
                }
            }
        }, new Action0() {
            @Override
            public void call() {
                System.out.println("Thread3 --> " + Thread.currentThread());
                System.out.println("Final complete");
                synchronized (obj) {
                    obj.notifyAll();
                }
            }
        });


        synchronized (obj) {
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Result {

        int code;

        Result code(int code) {
            this.code = code;
            return this;
        }

        @Override
        public String toString() {
            return "code: " + code;
        }
    }
}
