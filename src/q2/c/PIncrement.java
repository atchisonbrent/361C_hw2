package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {

    private static int totThreads;
    private static int threadCounter;
    private static AndersonLock l = new AndersonLock(0);
    private static AtomicInteger a = new AtomicInteger();

    public static class AndersonLock {

        AtomicInteger tailSlot = new AtomicInteger();
        boolean[] available;
        ThreadLocal<Integer> mySlot = new ThreadLocal<>();

        private AndersonLock(int n) {
            available = new boolean[n];
            for (int i = 0; i < available.length; i++) {
                if (i == 0) { available[i] = true; }
                else { available[i] = false; }
            }
        }

        private void lock() {
            mySlot.set(tailSlot.getAndIncrement() % totThreads);
            while (!available[mySlot.get()]) { /* Wait */ }
        }

        private void unlock() {
            a.set(a.get() + 1);
            available[mySlot.get()] = false;
            available[(mySlot.get() + 1) % totThreads] = true;
        }
    }

    static class myThread extends Thread {

        int count = 0;

        myThread () { /* Constructor */ }

        public void run () {
            while (count < threadCounter) {
                l.lock();
                l.unlock();
                count++;
            }
        }
    }

    public static int parallelIncrement(int c, int numThreads) {

        l = new AndersonLock(numThreads);
        totThreads = numThreads;

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
