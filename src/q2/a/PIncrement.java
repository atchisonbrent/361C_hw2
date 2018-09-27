package q2.a;

public class PIncrement {

    int n;
    int turn;
    int delta;
    static int m = 120000;

    public PIncrement(int numProc) {
        n = numProc;
        turn = -1;
        delta = 5;
    }

    public void requestCS(int i) {
        while (true) {
            while (turn != -1) { /* Wait for door to open */ }
            turn = i;
            try { Thread.sleep(delta); }
            catch (InterruptedException e) { };
            if (turn == i) { return; }
        }
    }

    public void releaseCS(int i) { turn = -1; }

    public static int parallelIncrement(int c, int numThreads) {
        for (; c < m; c++) {  }
        return c;
    }

}
