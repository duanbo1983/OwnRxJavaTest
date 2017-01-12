package test;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by bod on 1/11/17.
 */
public class ColdTest {

    public static void main(String[] strings) {
        final Observable<Integer> just = Observable.just(1, 2, 3);
        just.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("a: " + integer);
            }
        });
        just.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("b: " + integer);
            }
        });

    }
}
