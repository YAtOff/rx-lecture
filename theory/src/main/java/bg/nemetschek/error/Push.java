package bg.nemetschek.error;

import java.util.ArrayList;
import java.util.List;

public class Push {
    List<Runnable> tasks = new ArrayList<>();

    public static void main(String[] args) {
        Push p = new Push();
        p.doStuff();

        // event loop
        p.runTasks();
    }

    void doStuff() {
        try {
            schedule(this::step1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void step1() {
        try {
            Thread.currentThread().sleep(100);
            step2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void step2() {
        int x = 10 / 0;
    }

    void schedule(Runnable task) {
        tasks.add(task);
    }

    void runTasks() {
        tasks.forEach(task -> {
            try {
                new Thread(task).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

