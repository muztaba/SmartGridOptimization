package Utils;

import java.util.Arrays;

/**
 * Created by seal on 6/2/15.
 */
public class ArrayUtils {
    public static void fill(double[][] array, double value) {
        for (double[] row : array) {
            Arrays.fill(row, value);
        }
    }
}
