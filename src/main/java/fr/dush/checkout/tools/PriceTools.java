package fr.dush.checkout.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimal was used to prevent round issues. But to work, every BigDecimal must have the proper MathContext.
 * <p>
 * It would be simpler to use Long value in cents instead of BigDecimal.
 */
public class PriceTools {

    public static final int DECIMAL_NUMBER = 2;

    /** Make sure BigDecimal are limited to 2 decimals. */
    public static BigDecimal newBigDecimal(double val) {
        return round(new BigDecimal(val));
    }

    /** Force to round a BigDecimal */
    public static BigDecimal round(BigDecimal val) {
        return val.setScale(DECIMAL_NUMBER, RoundingMode.HALF_UP);
    }

}
