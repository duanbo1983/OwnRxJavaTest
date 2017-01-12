package test.backpressure;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import rx.Observable;

public class NoBackPressure {

    public static void main(String[] args) {
        final long producePeriod = 100;
        final long consumePeriod = 300;
        AtomicInteger pendingTaskCount = new AtomicInteger();

        // Create a fast producer emitting an infinite number of items.
        createStream(producePeriod, true, pendingTaskCount::incrementAndGet)
                .flatMap(ignoredIntegerValue ->
                        // Create a slow consumer emitting just one item.
                        createStream(consumePeriod, false, pendingTaskCount::decrementAndGet))
                .take(5)
                .toBlocking()
                .last();

        System.out.format("pending task count: %d\n", pendingTaskCount.get());
    }

    private static <T> Observable<T> createStream(long pausePeriodMillis, boolean infinite, Supplier<T> body) {
        return Observable.create(onSubscribe -> {
            new Thread() {
                @Override
                public void run() {
                    do {
                        pause(pausePeriodMillis);
                        T next = body.get();
                        onSubscribe.onNext(next);
                    } while (infinite && !onSubscribe.isUnsubscribed());
                }
            }.start();
        });
    }

    private static void pause(long millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException error) {
            System.out.println(error);
        }
    }

}