package bg.nemetschek.promise;

import java.util.function.Consumer;

public class Callback {

    public static void main(String[] args) {
        Callback.runAsync(result -> System.out.println("Result is " + result));
    }

    static void runAsync(Consumer<Integer> callback) {
        System.out.println("Doing stuff ...");
        callback.accept(42);
    }
}
