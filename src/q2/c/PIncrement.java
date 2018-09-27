package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {

    private static int totThreads;
    private static int threadCounter;
    private static AndersonLock l = new AndersonLock(1);
    private static AtomicInteger a = new AtomicInteger();

    public static class AndersonLock {

        AtomicInteger tailSlot = new AtomicInteger();
        boolean[] available;
        int mySlot;

        private AndersonLock(int n) {
            available = new boolean[n];
            for (int i = 0; i < available.length; i++) {
                if (i == 0) { available[i] = true; }
                else { available[i] = false; }
            }
        }

        public void lock() {
            mySlot = tailSlot.getAndIncrement() % totThreads;
            while (!available[mySlot]) { /* Wait */ }
        }

        public void unlock() {
            a.set(a.get() + 1);
            available[mySlot] = false;
            available[(mySlot + 1) % totThreads] = true;
        }
    }

    static class myThread extends Thread {

        int id;
        int count = 0;

        myThread (int i) { id = i; }

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
            myThread t = new myThread(i);
            t.start();
        }

        while (a.get() != m) { /* Wait until done incrementing */ }

        return a.get();
    }

}
