package bg.nemetschek.theory.rxjava;

import rx.Observable;

public class SideEffects {
    public static void main(String[] args) throws InterruptedException {
        Observable.range(1, 5)
                .doOnNext(i -> System.out.println("OBS after emission: " + i))
                .filter(i -> i % 2 == 0)
                .doOnNext(i -> System.out.println("OBS after filter: " + i))
                .map(i -> "___" + Integer.toString(i) + "___")
                .doOnNext(i -> System.out.println("OBS after map: " + i))
                .subscribe();
    }
}
