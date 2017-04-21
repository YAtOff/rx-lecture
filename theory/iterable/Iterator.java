package bg.nemetschek.iterable;

@FunctionalInterface
public interface Iterator<T> {
    Try<Maybe<T>> next();
}
