package test;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class FlatMapTest {

    public static void main(String[] strings) {

        Observable.just(1, 3, 5, 7, 9).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                Integer[] array = new Integer[integer];
                for (int i = 0; i < integer; i++) {
                    array[i] = i + 1;
                }
                return Observable.from(array);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });
    }
}
