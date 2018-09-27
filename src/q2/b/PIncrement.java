package q2.b;

public class PIncrement {

    static int m = 120000;

    public static int parallelIncrement(int c, int numThreads) {
        for (; c < m; c++) {  }
        return c;
    }

}
