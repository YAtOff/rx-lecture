package bg.nemetschek.duality;

import com.lambdista.util.Try;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        SimpleIterable<Integer> countTo5 = () -> {
            int[] i = {0};
            return () -> Try.apply(() -> i[0] > 4 ? Optional.empty() : Optional.of(++i[0]));
        };

        SimpleIterator<Integer> it = countTo5.iterator();
        while (true) {
            Try<Optional<Integer>> t = it.next();
            try {
                Optional<Integer> valueOrEnd = t.checkedGet();
                if (valueOrEnd.isPresent()) {
                    System.out.println(valueOrEnd.get());
                } else {
                    break;
                }
            } catch (Throwable e) {
                System.out.println(e);
                break;
            }
        }
    }
}
