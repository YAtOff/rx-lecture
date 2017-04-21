package bg.nemetschek.iterable;

class Maybe<T> {
    private final T valueOrNull;

    private Maybe(T valueOrNull) {
        this.valueOrNull = valueOrNull;
    }
    public static <T> Maybe<T> of(T a) {
        return new Maybe<T>(a);
    }
    public static <T> Maybe<T> empty() {
        return new Maybe<T>(null);
    }

    public boolean isEmpty() {
        return this.valueOrNull == null;
    }

    public T get() {
        return valueOrNull;
    }
}
