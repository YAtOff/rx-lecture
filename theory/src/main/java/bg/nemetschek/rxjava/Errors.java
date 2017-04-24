package bg.nemetschek.rxjava;

import rx.Observable;

public class Errors {
    public static void main(String[] args) throws InterruptedException {
        Observable.fromCallable(() -> 10 / 0)
                .doOnNext(System.out::println)
                .map(i -> i * 2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace
                );

        Observable.fromCallable(() -> 10 / 0)
                .onErrorReturn(error -> -1)
                .map(i -> i * 2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace
                );

        Observable.fromCallable(() -> 10 / 0)
                .onErrorResumeNext(Observable.just(-1))
                .map(i -> i * 2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace
                );

        Observable.fromCallable(() -> 10 / 0)
                .doOnError(error -> System.out.println("Error"))
                .map(i -> i * 2)
                .subscribe(
                        System.out::println,
                        Throwable::printStackTrace
                );

        Observable.just(1)
                .map(i -> i / 0)
                .subscribe(System.out::println);
    }
}
