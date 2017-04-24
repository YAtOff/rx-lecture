package bg.nemetschek.rxjava;

import rx.Observable;

public class Lazy {
    public static void main(String[] args) {
        Observable<Integer> numbers = Observable.fromCallable(() -> {
            System.out.println("Generating values ...");
            return 1;
        });
        numbers.subscribe(System.out::println);
    }
}
