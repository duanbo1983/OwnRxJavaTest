package test;

import rx.Observable;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultipleTaskRx {

    public static void main(String[] strings) {
        List<String> list = new ArrayList<>(20);
        for (int i = 0; i < 20; i++) {
            list.add("http://" + i);
        }
        new ImageDownloader()
                .download(list)
                .subscribe(
                        aBoolean -> System.out.println("Download finish? " + aBoolean));

        System.out.println("10s start");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("10s end");
    }

    private static class ImageDownloader {

        static Random random = new Random();

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        public Observable<Boolean> download(List<String> imageUrlList) {
            if (imageUrlList == null || imageUrlList.size() == 0) {
                return Observable.just(false);
            }

            return Observable.zip(Observable.from(imageUrlList)
                            .map(url -> downloadImage(url)),
                    args -> {
                        for (Object obj : args) {
                            if (!(obj instanceof Boolean) || !((Boolean) obj)) {
                                return false;
                            }
                        }
                        executorService.shutdownNow();
                        return true;
                    });
        }

        private Observable<Boolean> downloadImage(final String url) {
            final int sleep = (int) (random.nextFloat() * 1000);
            System.out.println("start downloading: " + url + ", duration: " + sleep);
            System.out.println("thread 0: " + Thread.currentThread());
            return Observable.defer(() -> {
                System.out.println("thread for download: " + Thread.currentThread());
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return Observable.just(true);
            }).subscribeOn(Schedulers.from(executorService));
        }
    }
}
