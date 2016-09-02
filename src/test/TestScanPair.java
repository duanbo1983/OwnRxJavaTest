package test;

import com.sun.tools.javac.util.Pair;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Created by bod on 9/1/16.
 */
public class TestScanPair {

    public static void main(String[] strings) {

        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);

        observable
                .compose(new PairTransformer<>(0))
                .subscribe(new Action1<Pair<Integer, Integer>>() {
                    @Override
                    public void call(Pair<Integer, Integer> integerIntegerPair) {
                        System.out.println(integerIntegerPair);
                    }
                });

        observable.scan(new TwoInt(), new Func2<TwoInt, Integer, TwoInt>() {
            @Override
            public TwoInt call(TwoInt twoInt, Integer integer) {
                TwoInt newTwo = new TwoInt();
                newTwo.i0 = twoInt.i1;
                newTwo.i1 = integer;
                return newTwo;
            }
        }).subscribe(new Action1<TwoInt>() {
            @Override
            public void call(TwoInt twoInt) {
                System.out.println(twoInt);
            }
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    public static <T> Observable<Pair<T, T>> pair(T initValue, Observable<T> observable) {
        Pair<T, T> pair = new Pair<>(initValue, initValue);
        return observable.scan(pair, new Func2<Pair<T, T>, T, Pair<T, T>>() {
            @Override
            public Pair<T, T> call(Pair<T, T> ttPair, T t) {
                return new Pair<>(ttPair.snd, t);
            }
        });
    }

    public static class PairTransformer<T> implements Observable.Transformer<T, Pair<T, T>> {

        private T initValue;

        PairTransformer(T initValue) {
            this.initValue = initValue;
        }

        @Override
        public Observable<Pair<T, T>> call(Observable<T> observable) {
            Pair<T, T> pair = new Pair<>(initValue, initValue);
            return observable.scan(pair, new Func2<Pair<T, T>, T, Pair<T, T>>() {
                @Override
                public Pair<T, T> call(Pair<T, T> ttPair, T t) {
                    return new Pair<>(ttPair.snd, t);
                }
            });
        }
    }

    private static class TwoInt {
        int i0;
        int i1;

        @Override
        public String toString() {
            return "i0: " + i0 + ", i1: " + i1;
        }
    }
}
