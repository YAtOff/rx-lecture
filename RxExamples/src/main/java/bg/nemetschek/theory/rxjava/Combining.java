package bg.nemetschek.theory.rxjava;

import rx.Observable;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Combining {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Observable.zip(
                Observable.interval(1, TimeUnit.SECONDS),
                Observable.just("One", "Two", "Three", "Four"),
                (a, b) -> a + " __ " + b
        ).subscribe(
                s -> System.out.println(LocalDateTime.now() + " ZIP " + s),
                Throwable::printStackTrace,
                () -> {
                    latch.countDown();
                    System.out.println(LocalDateTime.now() + " ZIP onComplete");
                }
        );

        latch.await();
    }
}
