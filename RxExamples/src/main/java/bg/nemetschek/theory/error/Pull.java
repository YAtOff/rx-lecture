package bg.nemetschek.theory.error;

public class Pull {
    public static void main(String[] args) {
        Pull p = new Pull();

        p.doStuff();
    }

    void doStuff() {
        try {
            step1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void step1() {
        step2();
    }

    void step2() {
        int x = 10 / 0;
    }
}
