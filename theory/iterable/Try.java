package bg.nemetschek.iterable;

import java.util.function.Supplier;

public interface Try<T> {
    boolean isSuccess();

    boolean isFailure();

    static <T> Try<T> apply(Supplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Throwable e) {
            if (e instanceof Exception) return new Failure<>((Exception) e);
            else throw ((Error) e);
        }
    }
}
