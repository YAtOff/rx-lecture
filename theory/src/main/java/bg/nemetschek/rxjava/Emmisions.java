package bg.nemetschek.rxjava;

import rx.Observable;

import java.util.ArrayList;

public class Emmisions {
    public static void main(String[] args) {
        System.out.println("All:");
        Observable.range(1, 10)
                .all(item -> item > 0)
                .subscribe(System.out::println);
        Observable.range(-1, 10)
                .all(item -> item > 0)
                .subscribe(System.out::println);

        System.out.println("Collect:");
        Observable.range(0, 10)
                .collect(ArrayList::new, ArrayList::add)
                .subscribe(System.out::println);

        System.out.println("Count:");
        Observable.just(1,5,3,5,3,1)
                .count()
                .subscribe(System.out::println);

        System.out.println("Distinct:");
        Observable.just(1,5,3,5,3,1)
                .distinct()
                .subscribe(System.out::println);
    }
}
