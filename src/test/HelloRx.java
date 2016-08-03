package test;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.internal.schedulers.NewThreadWorker;
import rx.internal.util.RxThreadFactory;
import rx.plugins.RxJavaObservableExecutionHook;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;

/**
 * Created by bod on 9/13/15.
 */
public class HelloRx {

    public static void main(String[] string) {
        RxJavaPlugins.getInstance().registerObservableExecutionHook(new RxJavaObservableExecutionHook() {
            @Override
            public <T> Observable.OnSubscribe<T> onCreate(Observable.OnSubscribe<T> f) {
                System.out.println("DB-Test onCreate --> Thread : " + Thread.currentThread().getId() + ", " + f);
                return super.onCreate(f);
            }

            @Override
            public <T> Observable.OnSubscribe<T> onSubscribeStart(
                    Observable<? extends T> observableInstance, Observable.OnSubscribe<T> onSubscribe) {
                System.out.println("DB-Test onSubscribeStart --> Thread : " + Thread.currentThread().getId() + ", " + onSubscribe);
                return super.onSubscribeStart(observableInstance, onSubscribe);
            }

            @Override
            public <T> Subscription onSubscribeReturn(Subscription subscription) {
                System.out.println("DB-Test onSubscribeReturn --> Thread : " + Thread.currentThread().getId() + ", " + subscription);
                return super.onSubscribeReturn(subscription);
            }

            @Override
            public <T> Throwable onSubscribeError(Throwable e) {
                System.out.println("DB-Test onSubscribeError --> Thread : " + Thread.currentThread().getId() + ", " + e);
                return super.onSubscribeError(e);
            }

            @Override
            public <T, R> Observable.Operator<? extends R, ? super T> onLift(Observable.Operator<? extends R, ? super T> lift) {
                System.out.println("DB-Test onLift --> Thread : " + Thread.currentThread().getId() + ", " + lift);
                return super.onLift(lift);
            }
        });


        RxJavaPlugins.getInstance().registerSchedulersHook(
                new RxJavaSchedulersHook() {
                    @Override
                    public Scheduler getComputationScheduler() {
                        return null;
                    }

                    @Override
                    public Scheduler getIOScheduler() {
                        return null;
                    }

                    @Override
                    public Scheduler getNewThreadScheduler() {
                        return new Scheduler() {
                            @Override
                            public Worker createWorker() {
                                return new NewThreadWorker(new RxThreadFactory("prefix"));
                            }
                        };
                    }

                    @Override
                    public Action0 onSchedule(Action0 action) {
                        WrappedAction0 wrappedAction0 = new WrappedAction0(action);
                        System.out.println("DB-Test onSchedule tag: " + RequestContext.getRequestContext().getTag() +
                                ", threadId: " + Thread.currentThread().getId() + ", wrappedAction0: " + wrappedAction0);
                        return super.onSchedule(wrappedAction0);
                    }
                });

        DataSource dataSource = new DataSource();
        System.out.println("DB-Test tag = request 0");
        RequestContext.getRequestContext().setTag("request 0");
        final Subscription subscription0 = dataSource
                .getUuidObservable()
                .observeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("DB-Test --> 0000 called tag: "
                                + RequestContext.getRequestContext().getTag()
                                + ", threadId: " + Thread.currentThread().getId() + ", v: " + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("DB-Test --> 0000 error  tag: "
                                + RequestContext.getRequestContext().getTag()
                                + ", threadId: " + Thread.currentThread().getId() + ", e: " + throwable);
                    }
                });

        System.out.println("DB-Test tag = request 1");
        RequestContext.getRequestContext().setTag("request 1");
        final Subscription subscription1 = dataSource.getUuidObservable().distinctUntilChanged()
                .observeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("DB-Test --> 1111 called tag: "
                                + RequestContext.getRequestContext().getTag()
                                + ", threadId: " + Thread.currentThread().getId() + ", v: " + s);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        System.out.println("DB-Test --> 1111 error  tag: "
                                + RequestContext.getRequestContext().getTag()
                                + ", threadId: " + Thread.currentThread().getId() + ", e: " + throwable);
                    }
                });

        dataSource.setUuid("123");
        dataSource.setUuid("456");
        dataSource.onError();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        subscription0.unsubscribe();
        subscription1.unsubscribe();
        System.out.println("end");
    }

    private static class WrappedAction0 implements Action0 {

        private final Action0 actual;

        private final String tag;

        public WrappedAction0(Action0 actual) {
            this.actual = actual;
            this.tag = RequestContext.getRequestContext().getTag();
        }

        @Override
        public void call() {
            RequestContext.getRequestContext().setTag(tag);
            System.out.println("DB-Test exe actual call in thread: " + Thread.currentThread().getId()
                    +  ", tag: " + tag + ", " + "WrappedAction0: " + this);
            try {
                actual.call();
            } catch (Exception e) {
                RequestContext.getRequestContext().setTag(null);
            }
        }
    }
}

