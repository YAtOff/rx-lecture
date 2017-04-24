package bg.nemetschek.rxjava;

import rx.Observable;

public class Accumulation {
    public static void main(String[] args) {
        System.out.println("Reduce:");
        Observable.range(1, 10)
            .reduce((acc, val) -> acc + val)
            .subscribe(System.out::println);

        System.out.println("Scan:");
        Observable.range(1, 10)
                .scan((acc, val) -> acc + val)
                .subscribe(System.out::println);
    }
}
