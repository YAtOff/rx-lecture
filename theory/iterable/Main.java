package bg.nemetschek.iterable;

public class Main {
    public static void main(String[] args) {
        Iterable<Integer> countTo5 = () -> {
            int[] i = {0};
            return () -> Try.apply(() -> Maybe.of(i[0] > 4 ? null : ++i[0]));
        };

        Iterator<Integer> it = countTo5.iterator();
        while (true) {
            Try<Maybe<Integer>> t = it.next();
            if (t.isSuccess()) {
                Success<Maybe<Integer>> s = (Success<Maybe< Integer>>) t;
                Maybe<Integer> v = s.get();
                if (v.isEmpty()) {
                    break;
                }
                System.out.println(v.get());
            }

        }
    }
}
