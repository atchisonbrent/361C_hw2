package q2.a;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {

    private static int threadCounter;
    private static AtomicInteger a = new AtomicInteger();
    private static AtomicInteger turn = new AtomicInteger(-1);

    private static void requestCS(int i) {
        while (true) {
            while (turn.get() != -1) { /* Wait for door to open */ }
            turn.set(i);
            try { Thread.sleep(0, 10000); }
            catch (InterruptedException e) { }
            if (turn.get() == i) { return; }
        }
    }

    private static void releaseCS() {
        a.set(a.get() + 1);
        turn.set(-1);
    }

    static class myThread extends Thread {

        int count = 0;

        public void run () {
            while (count < threadCounter) {
                requestCS((int)this.getId());
                releaseCS();
                count++;
            }
        }

    }

    public static int parallelIncrement(int c, int numThreads) {

        /* What to Count to */
        int m = 120000;

        /* Set Initial Value of Counter */
        a.set(c);
        threadCounter = m / numThreads;

        /* Start Threads in Loop */
        for (int i = 0; i < numThreads; i++) {
            myThread t = new myThread();
            t.start();
        }

        while (a.get() != m) { /* Wait until done incrementing */ }

        return a.get();
    }

}
