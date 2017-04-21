package bg.nemetschek.iterable;

@FunctionalInterface
public interface Iterable<T> {
    Iterator<T> iterator();
}
