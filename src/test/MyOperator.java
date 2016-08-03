package test;

import rx.Observable;
import rx.Subscriber;

public class MyOperator<R, T> implements Observable.Operator<R, T> {
    public MyOperator( /* any necessary params here */) {
    /* any necessary initialization here */
    }

    @Override
    public Subscriber<? super T> call(Subscriber<? super R> subscriber) {
        return null;
    }
}
