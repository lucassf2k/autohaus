package shared;

import java.text.NumberFormat;

public class CurrencyFormatter {
    public static String real(final Double amount) {
        return NumberFormat
                .getCurrencyInstance()
                .format(amount);
    }
}
