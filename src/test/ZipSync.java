package test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observables.ConnectableObservable;

public class ZipSync {

    public static void main(String[] strings) {

        ConnectableObservable<Long> locations = Observable.interval(200, TimeUnit.MILLISECONDS).replay(5);

        locations.connect();
        Observable<String> networkRequests = Observable.interval(1000, TimeUnit.MILLISECONDS).map(
                new Func1<Long, String>() {
                    @Override
                    public String call(Long aLong) {
                        System.out.println("time -->");
                        return aLong + "";
                    }
                });

        Observable<Integer> response = Observable.zip(locations.buffer(5), networkRequests,
                new Func2<List<Long>, String, Integer>() {
                    @Override
                    public Integer call(List<Long> longs, String s) {
                        System.out.println(s + ": " + longs);
                        return longs.size();
                    }
                });
        response.subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println(integer);
            }
        });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
