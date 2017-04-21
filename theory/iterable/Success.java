package bg.nemetschek.iterable;

public class Success<T> implements Try<T> {
    private T val;

    public Success(T val) {
        this.val = val;
    }

    public T get() {
        return this.val;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
    @Override
    public boolean isFailure() {
        return false;
    }
}
