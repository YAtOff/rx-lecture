package bg.nemetschek.theory.duality;

import com.lambdista.util.Try;

import java.util.Optional;

@FunctionalInterface
public interface SimpleIterator<T> {
    Try<Optional<T>> next();
}
