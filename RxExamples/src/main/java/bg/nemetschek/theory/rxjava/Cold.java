package bg.nemetschek.theory.rxjava;

import rx.Observable;

public class Cold {
    public static void main(String[] args) {
        Observable<Integer> numbers = Observable.from(new Integer[] {1, 2, 3});
        numbers.subscribe(System.out::println);
        numbers.subscribe(System.out::println);
    }
}
