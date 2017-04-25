package bg.nemetschek.theory.promise;

import java.util.function.Consumer;
import java.util.function.Function;

public class Promise <T> {
    private T result;

    public static void main(String[] args) {
        Promise<Integer> promise = Promise.runAsync(1);
        promise.thenAccept(result -> System.out.println("Result is " + result));
    }

    static Promise<Integer> runAsync(int source) {
        Promise<Integer> promise = new Promise<>();
        System.out.println("Doing stuff ...");
        promise.result = 42;
        return promise;
    }

    void thenAccept(Consumer<T> callback) {
        callback.accept(result);
    }

    <U> Promise<U> thenApply(Function<T, U> transformer) {
        return null;
        // executes next when current promise is resolved
        // creates new promise that holds the result with transformer applied to in
    }


    <U> Promise<U> thenCompose(Function<T, Promise<U>> next) {
        return null;
        // executes next when current promise is resolved
        // creates new promise that is resolved when next is ready
    }
}
