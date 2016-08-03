package com.ubercab.network.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

public class MultipleTaskRx {

    public static void main(String[] strings) {
        List<String> list = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            list.add("http://" + i);
        }
        new ImageDownloader()
                .download(list)
                .subscribe(
                        new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                System.out.println("Download finish? " + aBoolean);
                            }
                        });

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class ImageDownloader {

        static Random random = new Random();

        Scheduler mScheduler = Schedulers.from(Executors.newFixedThreadPool(3));

        public Observable<Boolean> download(List<String> imageUrlList) {
            if (imageUrlList == null || imageUrlList.size() == 0) {
                return Observable.just(false);
            }

            return Observable.zip(Observable.from(imageUrlList)
                            .map(new Func1<String, Observable<Boolean>>() {
                                @Override
                                public Observable<Boolean> call(String url) {
                                    return downloadImage(url);
                                }
                            }),
                    new FuncN<Boolean>() {
                        public Boolean call(Object... args) {
                            for (Object obj : args) {
                                if (!(obj instanceof Boolean) || !((Boolean) obj)) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    });
        }

        private Observable<Boolean> downloadImage(final String url) {
            final int sleep = (int) (random.nextFloat() * 5000);
            System.out.println("start downloading: " + url + ", duration: " + sleep);
            System.out.println("thread 0: " + Thread.currentThread());
            return Observable.defer(new Func0<Observable<Boolean>>() {
                @Override
                public Observable<Boolean> call() {
                    System.out.println("thread for download: " + Thread.currentThread());
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    return Observable.just(true);
                }
            }).subscribeOn(mScheduler);
        }
    }
}
