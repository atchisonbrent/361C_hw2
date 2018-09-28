package q2.b;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement {

    private static int threadCounter;
    private static AtomicInteger a = new AtomicInteger();
    private static AtomicInteger X = new AtomicInteger(-1);
    private static AtomicInteger Y = new AtomicInteger(-1);
    private static boolean[] flag = new boolean[0];

    /* Check if any Values in Array are True */
    private static boolean areAnyTrue(boolean[] array) {
        for(boolean b : array) if (b) return true;
        return false;
    }

    private static void requestCS(int i) {
        while (true) {
            flag[i] = true;
            X.set(i);
            if (Y.get() != -1) {
                flag[i] = false;
                while (Y.get() != -1) { /* Wait for Y */ }
            }
            else {
                Y.set(i);
                if (X.get() == i) { return; }       /* Fast Path */
                else {
                    flag[i] = false;
                    while (areAnyTrue(flag)) { /* Wait for All Flags to be False */ }
                    if (Y.get() == i) { return; }   /* Slow Path */
                    else {
                        while (Y.get() != -1) { /* Wait for Y */ }
                    }
                }
            }
        }
    }

    private static void releaseCS(int i) {
        a.set(a.get() + 1);
        Y.set(-1);
        flag[i] = false;
    }

    static class myThread extends Thread {

        int id;
        int count = 0;

        myThread (int i) { id = i; }

        public void run () {
            while (count < threadCounter) {
                requestCS(this.id);
                releaseCS(this.id);
                count++;
            }
        }

    }

    public static int parallelIncrement(int c, int numThreads) {

        /* Flag Array Instantiated to False */
        flag = new boolean[numThreads];
        for (int i = 0; i < flag.length; i++) { flag[i] = false; }

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

        while (a.get() != m + c) { /* Wait until done incrementing */ }

        return a.get();
    }

}
