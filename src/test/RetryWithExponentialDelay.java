package test;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Defines utility class to handle retry with RX.
 */
public class RetryWithExponentialDelay implements Func1<Observable<? extends Throwable>, Observable<?>> {

    private static final long MIN_DELAY_MS = 100;
    private static final long MAX_DELAY_MS = 16000;
    private static final long MULTIPLIER = 2;

    private final int maxRetries;

    /**
     * Constructor.
     *
     * @param maxRetries the max retry times.
     */
    public RetryWithExponentialDelay(final int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> throwable) {
        return retryFunc(maxRetries).call(throwable);
    }

    static long getDelayMs(int attempts) {
        return Math.min((long) (MIN_DELAY_MS * Math.pow(MULTIPLIER, attempts - 1)), MAX_DELAY_MS);
    }

    private static Func1<? super Observable<? extends Throwable>, ? extends Observable<?>> retryFunc(final int maxRetries) {
        return new Func1<Observable<? extends Throwable>, Observable<Long>>() {
            @Override
            public Observable<Long> call(final Observable<? extends Throwable> observable) {
                // zip our number of retries to the incoming errors so that we only produce retries
                // when there's been an error
                return observable.zipWith(
                        Observable.range(1, maxRetries > 0 ? (maxRetries + 1): Integer.MAX_VALUE),
                        new Func2<Throwable, Integer, RetryState>() {
                            @Override
                            public RetryState call(Throwable throwable, Integer attemptNumber) {
                                return new RetryState().setAttemptNumber(attemptNumber).setThrowable(throwable);
                            }
                        })
                        // flatMap the int attempt number to a timer that will wait the specified delay
                        .flatMap(new Func1<RetryState, Observable<Long>>() {
                            @Override
                            public Observable<Long> call(final RetryState retryState) {
                                if (retryState.attemptNumber <= maxRetries) {
                                    return Observable.timer(getDelayMs(retryState.attemptNumber), TimeUnit.MILLISECONDS);
                                } else {
                                    // Max retries hit. Just pass the error along.
                                    return Observable.error(retryState.throwable);
                                }
                            }
                        });
            }
        };
    }

    private static class RetryState {
        int attemptNumber;
        Throwable throwable;

        public RetryState setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public RetryState setAttemptNumber(int attemptNumber) {
            this.attemptNumber = attemptNumber;
            return this;
        }
    }
}