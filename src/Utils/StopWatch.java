package Utils;

/**
 * Created by seal on 7/31/15.
 */

public class StopWatch {
    private static double start, end;
    private static final double h = 1000000000.0;

    //	public static final double h = 1000.0;
    public static void start() {
        init();
        start = System.nanoTime();
//		start = System.currentTimeMillis();
    }

    public static double elapsedTime() {
        end = System.nanoTime();
//		end = System.currentTimeMillis();
        double result = (end - start) / h;
        System.out.println(result);
        init();
        return result;
    }

    private static void init() {
        start = end = 0.0;
    }
}
