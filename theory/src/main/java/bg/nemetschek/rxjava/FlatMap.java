package bg.nemetschek.rxjava;

import rx.Observable;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FlatMap {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("FlatMap:");
        Observable.range(1, 3)
                .flatMap(i -> Observable.just("a_" + i, "b_" + i))
                .subscribe(System.out::println);

        CountDownLatch latch = new CountDownLatch(1);
        Observable.range(1, 3)
                .flatMap(i ->
                        Observable.interval(i, TimeUnit.SECONDS)
                                .take(4)
                                .map(j -> String.format("[%d, %d]", i, j)))
                .subscribe(
                        s -> System.out.println(LocalDateTime.now() + " Emitted " + s),
                        Throwable::printStackTrace,
                        () -> latch.countDown()
                );
        latch.await();

        CountDownLatch latch2 = new CountDownLatch(1);
        Observable.range(1, 3)
                .concatMap(i ->
                        Observable.interval(i, TimeUnit.SECONDS)
                                .take(4)
                                .map(j -> String.format("[%d, %d]", i, j)))
                .subscribe(
                        s -> System.out.println(LocalDateTime.now() + " Emitted " + s),
                        Throwable::printStackTrace,
                        () -> latch2.countDown()
                );
        latch2.await();
    }
}
