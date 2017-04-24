package bg.nemetschek.error;

import java.util.LinkedList;
import java.util.Queue;

public class Push {
    Queue<Runnable> tasks = new LinkedList<>();

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
        schedule(this::step2);
    }

    void step2() {
        int x = 10 / 0;
    }

    void schedule(Runnable task) {
        tasks.add(task);
    }

    void runTasks() {
        while (!tasks.isEmpty()) {
            Runnable task = tasks.remove();
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

