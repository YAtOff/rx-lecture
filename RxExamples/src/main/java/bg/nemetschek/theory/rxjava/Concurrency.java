package bg.nemetschek.theory.rxjava;

import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.CountDownLatch;

public class Concurrency {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        log("Starting");
        Observable<Integer> o = Observable.just(1);
        log("Created");
        o.subscribeOn(Schedulers.computation())
                .map(i -> i + 1)
                .subscribe(
                        i -> log(i.toString()),
                        Throwable::printStackTrace,
                        () -> {
                            log("Completed");
                            latch.countDown();
                        }
                );
        latch.await();
        log("Exiting");
    }

    static void log(String msg) {
        System.out.println(Thread.currentThread().getName() + " " + msg);
    }
}
