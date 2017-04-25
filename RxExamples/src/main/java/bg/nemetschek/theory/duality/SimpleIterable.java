package bg.nemetschek.theory.duality;

@FunctionalInterface
public interface SimpleIterable<T> {
    SimpleIterator<T> iterator();
}
