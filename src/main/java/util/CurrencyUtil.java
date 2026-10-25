package util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/** Format mata uang Rupiah, contoh: Rp5.000 atau Rp5.000,50. */
public final class CurrencyUtil {

    private CurrencyUtil() {
    }

    public static String format(BigDecimal amount) {
        if (amount == null) {
            amount = BigDecimal.ZERO;
        }
        if (amount.signum() < 0) {
            return "-" + format(amount.negate());
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        boolean whole = amount.stripTrailingZeros().scale() <= 0;
        DecimalFormat df = new DecimalFormat(whole ? "#,##0" : "#,##0.00", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return "Rp" + df.format(amount);
    }
}
