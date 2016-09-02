package test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * Created by bod on 9/1/16.
 */
public class TestScanPair {

    public static void main(String[] strings) {

        Observable<Integer> observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8);

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

    private static class TwoInt {
        int i0;
        int i1;

        @Override
        public String toString() {
            return "i0: " + i0 + ", i1: " + i1;
        }
    }
}
