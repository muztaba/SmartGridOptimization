package Utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by seal on 9/5/15.
 */
public class MathUtils {

    public static double setPrecisions(double val, int precisions) {
        BigDecimal bd = new BigDecimal(val).setScale(precisions, RoundingMode.DOWN);
        return bd.doubleValue();
    }

    public static double nextBigDigit(double val) {
        double i = 1.0;
        for (i = 1; i < val; i *= 10.0) {}
        return i;
    }

}
