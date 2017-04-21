package bg.nemetschek.iterable;

public class Failure<T> implements Try<T> {
    private Throwable exception;

    public Failure(Throwable exception) {
        this.exception = exception;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
    @Override
    public boolean isFailure() {
        return true;
    }
}

