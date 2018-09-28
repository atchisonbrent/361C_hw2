package q3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

    // declare the variables here
    // A monkey calls the method when it arrives at the river bank and wants to climb
    // the rope in the specified direction (0 or 1); Kong’s direction is -1.
    // The method blocks a monkey until it is allowed to climb the rope.
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition isEmpty = lock.newCondition();
    private static AtomicInteger numMonkeys = new AtomicInteger();
    private static AtomicInteger dir = new AtomicInteger(-2);
    private final int maxMonkeys = 3;

    public Monkey() {

    }

    public void ClimbRope(int direction) throws InterruptedException {

        lock.lock();

        /* Set Initial Direction */
        if (dir.get() == -2) { dir.set(direction); }

        try {
            if (direction == dir.get()) {   /* Check for Same Direction */
                while (numMonkeys.get() == maxMonkeys) { notFull.await(); }
                numMonkeys.set(numMonkeys.get() + 1);
            }
            else {  /* Different Direction */
                while (numMonkeys.get() > 0) { isEmpty.await(); }
                dir.set(direction);
                numMonkeys.set(numMonkeys.get() + 1);
            }
        } finally { lock.unlock(); }
    }

    public void LeaveRope() {
        lock.lock();
        try {
            numMonkeys.set(numMonkeys.get() - 1);
            if (numMonkeys.get() == 0) { isEmpty.signal(); }
            notFull.signal();
        } finally { lock.unlock(); }
    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
        return numMonkeys.get();
    }

}
