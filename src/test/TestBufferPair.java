package test;

import com.sun.tools.javac.util.Pair;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bod on 9/1/16.
 */
public class TestBufferPair {

    public static void main(String[] strings) {

        Observable<Long> observable = Observable.interval(1, TimeUnit.SECONDS);

        observable.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                System.out.println("raw: " + aLong);
            }
        });

        observable.buffer(2, 1)
                .subscribe(new Action1<List<Long>>() {
                    @Override
                    public void call(List<Long> longs) {
                        System.out.println(longs);
                    }
                });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class PairTransformer<T> implements Observable.Transformer<T, Pair<T, T>> {

        @Override
        public Observable<Pair<T, T>> call(Observable<T> observable) {
            return observable.buffer(2, 1).map(new Func1<List<T>, Pair<T, T>>() {
                @Override
                public Pair<T, T> call(List<T> ts) {
                    return new Pair<T, T>(ts.get(0), ts.size() > 1 ? ts.get(1) : null);
                }
            });
        }
    }
}
