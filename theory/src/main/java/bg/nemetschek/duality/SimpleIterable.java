package bg.nemetschek.duality;

@FunctionalInterface
public interface SimpleIterable<T> {
    SimpleIterator<T> iterator();
}
